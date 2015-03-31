package com.octagon.airships.client.gui.machine;


import cofh.api.energy.IEnergyContainerItem;
import com.octagon.airships.block.tileentity.TileEntityMachineBase;
import com.octagon.airships.client.gui.slots.SlotBattery;
import com.octagon.airships.client.gui.slots.SlotOutput;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import openmods.container.ContainerBase;

public class ContainerMachineBase<T extends TileEntityMachineBase> extends ContainerBase<T> {

    protected T machine;
    private int lastEnergyStored;
    private int lastMaxEnergyStored;

    public ContainerMachineBase(InventoryPlayer inventory, T machine) {
        super(inventory, machine.getInventory(), machine);
        this.machine = machine;
        addSlots();

        addPlayerInventorySlots(84);
    }

    protected void addSlots() {
        this.addSlotToContainer(new SlotBattery(machine.getInventory(), 0, 8, 53));
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting)
    {
        super.addCraftingToCrafters(crafting);
        crafting.sendProgressBarUpdate(this, 0, this.machine.getEnergyStored(null));
        crafting.sendProgressBarUpdate(this, 1, this.machine.getMaxEnergyStored(null));
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if (this.lastEnergyStored != this.machine.getEnergyStored(null))
            {
                icrafting.sendProgressBarUpdate(this, 0, this.machine.getEnergyStored(null));
            }

            if (this.lastMaxEnergyStored != this.machine.getMaxEnergyStored(null))
            {
                icrafting.sendProgressBarUpdate(this, 1, this.machine.getMaxEnergyStored(null));
            }
        }

        this.lastEnergyStored = this.machine.getEnergyStored(null);
        this.lastMaxEnergyStored = this.machine.getMaxEnergyStored(null);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int value)
    {
        if (id == 0)
        {
            this.machine.setEnergyStored(value);
        }

        if (id == 1)
        {
            this.machine.setMaxEnergyStored(value);
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slot instanceof SlotOutput)
            {
                if (!this.mergeItemStack(itemstack1, inventorySlots.size() - 36, inventorySlots.size(), true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (slotId >= inventorySlots.size() - 36)
            {
                if (itemstack1.getItem() instanceof IEnergyContainerItem)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (slotId >= inventorySlots.size() - 36 && slotId < inventorySlots.size() - 9)
                {
                    if (!this.mergeItemStack(itemstack1, inventorySlots.size() - 9, inventorySlots.size(), false))
                    {
                        return null;
                    }
                }
                else if (slotId >= inventorySlots.size() - 9 && slotId < inventorySlots.size() && !this.mergeItemStack(itemstack1, inventorySlots.size() - 36, inventorySlots.size() - 9, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, inventorySlots.size() - 36, inventorySlots.size(), false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }
}
