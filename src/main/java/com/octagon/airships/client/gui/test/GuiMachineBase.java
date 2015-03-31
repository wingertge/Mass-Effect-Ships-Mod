package com.octagon.airships.client.gui.test;

import cofh.api.energy.IEnergyContainerItem;
import com.octagon.airships.block.tileentity.TileEntityMachineBase;
import com.octagon.airships.client.gui.GuiAirshipsContainer;
import com.octagon.airships.client.gui.components.GuiComponentProgressBar;
import com.octagon.airships.client.gui.components.GuiComponentTooltip;
import com.octagon.airships.client.gui.slots.SlotBattery;
import com.octagon.airships.client.gui.slots.SlotOutput;
import com.octagon.airships.reference.Textures;
import com.octagon.airships.util.LangUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import javafx.geometry.Orientation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.container.ContainerBase;
import openmods.gui.component.BaseComposite;

import java.util.ArrayList;
import java.util.List;

public class GuiMachineBase extends GuiAirshipsContainer {
    public GuiMachineBase(Container container, String title) {
        super(container, 176, 166, title);
    }

    public GuiMachineBase(Container container) {
        this(container, "masseffectships.gui.machineBase");
    }

    private void updateDisplayElements(TileEntityMachineBase machine, GuiComponentTooltip tooltip, GuiComponentProgressBar energyLevel) {
        List<String> newLines = new ArrayList<>();
        newLines.add(LangUtil.localize("gui.powerLevel", true));
        newLines.add(StatCollector.translateToLocalFormatted("masseffectships.misc.energyStorage", machine.getEnergyStored(null), machine.getMaxEnergyStored(null)));
        tooltip.setText(newLines);
        float progress = (float)machine.getEnergyStored(ForgeDirection.NORTH) / (float)machine.getMaxEnergyStored(ForgeDirection.NORTH);
        energyLevel.setProgress(progress);
    }

    public void init(BaseComposite root) {
        final GuiComponentProgressBar energyLevel = new GuiComponentProgressBar(9, 8, 14, 42, Orientation.VERTICAL, false);
        energyLevel.setActiveTexture(Textures.Gui.MACHINE_ELEMENTS, 0, 0);
        energyLevel.setBackgroundTexture(Textures.Gui.MACHINE_ELEMENTS, 14, 0);
        root.addComponent(energyLevel);

        final GuiComponentTooltip tooltip = new GuiComponentTooltip(10, 9, 22, 49);
        final List<String> lines = new ArrayList<>();
        lines.add(LangUtil.localize("gui.powerLevel", true));
        tooltip.setText(lines);
        root.addComponent(tooltip);

        dispatcher().triggerAll();
    }

    public static class Container extends ContainerBase<TileEntityMachineBase> {
        protected TileEntityMachineBase machine;
        private int lastEnergyStored;
        private int lastMaxEnergyStored;

        public Container(IInventory inventory, TileEntityMachineBase machine) {
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

            for (Object crafter : this.crafters) {
                ICrafting icrafting = (ICrafting) crafter;

                if (this.lastEnergyStored != this.machine.getEnergyStored(null)) {
                    icrafting.sendProgressBarUpdate(this, 0, this.machine.getEnergyStored(null));
                }

                if (this.lastMaxEnergyStored != this.machine.getMaxEnergyStored(null)) {
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
}
