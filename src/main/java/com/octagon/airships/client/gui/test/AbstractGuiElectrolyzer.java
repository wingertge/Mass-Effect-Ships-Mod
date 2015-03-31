package com.octagon.airships.client.gui.test;

import com.octagon.airships.block.tileentity.TileEntityElectrolyzer;
import com.octagon.airships.client.gui.CrazyContainer;
import com.octagon.airships.client.gui.GuiAirshipsContainer;
import com.octagon.airships.client.gui.components.GuiComponentEnergyGauge;
import com.octagon.airships.client.gui.components.GuiComponentFluidGauge;
import com.octagon.airships.client.gui.slots.SlotBattery;
import com.octagon.airships.client.gui.slots.SlotLiquidContainer;
import net.minecraft.inventory.IInventory;
import openmods.gui.component.BaseComposite;

public class AbstractGuiElectrolyzer extends GuiAirshipsContainer<AbstractGuiElectrolyzer.Container> {
    protected GuiComponentEnergyGauge energyBar;
    protected GuiComponentFluidGauge inputTank;
    protected GuiComponentFluidGauge outputTank1;
    protected GuiComponentFluidGauge outputTank2;

    public AbstractGuiElectrolyzer(Container container) {
        super(container, 176, 166, "electrolyzer");
        init(container, createRoot());
    }

    public void init(Container container, BaseComposite root) {
        TileEntityElectrolyzer tileEntity = container.getOwner();
        energyBar = new GuiComponentEnergyGauge(9, 8);
        energyBar.setHeight(42);
        energyBar.setEnergyStorage(tileEntity.getEnergyStorage());
        root.addComponent(energyBar);

        inputTank = new GuiComponentFluidGauge(63, 8);
        inputTank.setWidth(14);
        inputTank.setHeight(42);
        inputTank.setTank(tileEntity.getInputTank());
        root.addComponent(inputTank);

        outputTank1 = new GuiComponentFluidGauge(117, 8);
        outputTank1.setWidth(14);
        outputTank1.setHeight(20);
        outputTank1.setTank(tileEntity.getOutputTank1());
        root.addComponent(outputTank1);

        outputTank2 = new GuiComponentFluidGauge(117, 30);
        outputTank2.setWidth(14);
        outputTank2.setHeight(20);
        outputTank2.setTank(tileEntity.getOutputTank2());
        root.addComponent(outputTank2);

        postInit(root);
    }

    public void postInit(BaseComposite root) {}

    public class Container extends CrazyContainer<TileEntityElectrolyzer> {
        public Container(IInventory playerInventory, TileEntityElectrolyzer owner) {
            super(playerInventory, owner.getInventory(), owner);
            addSlotToContainer(new SlotBattery(owner.getInventory(), 0, 8, 53));
            addSlotToContainer(new SlotLiquidContainer(owner.getInventory(), 1, 61, 52));
            addSlotToContainer(new SlotLiquidContainer(owner.getInventory(), 2, 30, 40));
            addPlayerInventorySlots(8, 84);
        }
    }
}
