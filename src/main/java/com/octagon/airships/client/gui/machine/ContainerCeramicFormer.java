package com.octagon.airships.client.gui.machine;

import com.octagon.airships.block.tileentity.TileEntityCeramicFormer;
import com.octagon.airships.client.gui.slots.SlotCFRCInput;
import com.octagon.airships.client.gui.slots.SlotCeramicFormerInput;
import com.octagon.airships.client.gui.slots.SlotOutput;
import com.octagon.airships.item.ItemCFRCPlate;
import com.octagon.airships.recipe.RecipesCeramicFormer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCeramicFormer extends ContainerMachineBase<TileEntityCeramicFormer> {

    private int lastCurrentWork;
    private int lastMaxWork;

    public ContainerCeramicFormer(InventoryPlayer inventory, TileEntityCeramicFormer machine) {
        super(inventory, machine);
    }

    @Override
    protected void addSlots() {
        super.addSlots();
        this.addSlotToContainer(new SlotCFRCInput(machine, 1, 62, 19));
        this.addSlotToContainer(new SlotCeramicFormerInput(machine, 2, 62, 44));
        this.addSlotToContainer(new SlotOutput(machine, 3, 108, 32));
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
                if(item1.getItem().getClass().equals(ItemCFRCPlate.class)) {
                    if(!this.mergeItemStack(item1, 1, 2, false))
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
                } else if(RecipesCeramicFormer.isItemValid(item1)) {
                    if(!this.mergeItemStack(item1, 2, 3, false))
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
        }
        return super.transferStackInSlot(player, slotId);
    }
}
