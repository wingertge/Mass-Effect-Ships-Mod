package com.octagon.airships.client.gui.machine;

import com.octagon.airships.block.tileentity.TileEntityAlloyMixer;
import com.octagon.airships.client.gui.slots.SlotInput;
import com.octagon.airships.client.gui.slots.SlotOutput;
import com.octagon.airships.recipe.RecipesAlloyMixer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAlloyMixer extends ContainerMachineBase<TileEntityAlloyMixer> {
    private int lastCurrentWork;
    private int lastMaxWork;

    public ContainerAlloyMixer(InventoryPlayer inventory, TileEntityAlloyMixer machine) {
        super(inventory, machine);
    }

    @Override
    protected void addSlots() {
        super.addSlots();
        this.addSlotToContainer(new SlotInput(machine.getInventory(), 1, 60, 16));
        this.addSlotToContainer(new SlotInput(machine.getInventory(), 2, 81, 16));
        this.addSlotToContainer(new SlotInput(machine.getInventory(), 3, 102, 16));
        this.addSlotToContainer(new SlotOutput(machine.getInventory(), 4, 81, 53));
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting)
    {
        super.addCraftingToCrafters(crafting);
        crafting.sendProgressBarUpdate(this, 2, this.machine.getCurrentWork());
        crafting.sendProgressBarUpdate(this, 3, this.machine.getMaxWork());
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if (this.lastCurrentWork != this.machine.getCurrentWork())
            {
                icrafting.sendProgressBarUpdate(this, 2, this.machine.getCurrentWork());
            }

            if (this.lastMaxWork != this.machine.getMaxWork())
            {
                icrafting.sendProgressBarUpdate(this, 3, this.machine.getMaxWork());
            }
        }

        this.lastCurrentWork = this.machine.getCurrentWork();
        this.lastMaxWork = this.machine.getMaxWork();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int value)
    {
        super.updateProgressBar(id, value);

        if (id == 2)
        {
            this.machine.setCurrentWork(value);
        }

        if (id == 3)
        {
            this.machine.setMaxWork(value);
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack item1 = slot.getStack();
            itemstack = item1.copy();

            if (slotId >= inventorySlots.size() - 36 && slotId < inventorySlots.size()) {
                boolean inserted = false;
                ItemStack[] input = new ItemStack[3];
                for (int i = 0; i < input.length; i++) {
                    Slot inputSlot = (Slot)this.inventorySlots.get(i + 1);
                    if(inputSlot != null && inputSlot.getHasStack())
                        input[i] = inputSlot.getStack();
                    else input[i] = null;

                    if(input[i] == null && !inserted) {
                        input[i] = item1;
                        inserted = true;
                    } else if(!inserted && input[i].getItem().equals(item1.getItem())) inserted = true;
                }

                if(inserted && RecipesAlloyMixer.isValidRecipePartial(input))
                    if(!this.mergeItemStack(item1, 1, 4, false))
                        return null;
                    else {
                        if (item1.stackSize == 0) {
                            slot.putStack(null);
                        } else {
                            slot.onSlotChanged();
                        }

                        if (item1.stackSize == itemstack.stackSize) {
                            return null;
                        }

                        slot.onPickupFromSlot(player, item1);

                        return itemstack;
                    }
            }
        }
        return super.transferStackInSlot(player, slotId);
    }
}
