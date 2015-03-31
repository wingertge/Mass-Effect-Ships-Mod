package com.octagon.airships.block.tileentity;

import com.octagon.airships.block.item.ItemMachine;
import com.octagon.airships.reference.Config;
import com.octagon.airships.sync.MonitoredTank;
import com.octagon.airships.sync.SyncableEnergyStorage;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import openmods.include.IncludeInterface;
import openmods.inventory.GenericInventory;
import openmods.inventory.TileEntityInventory;
import openmods.liquids.SidedFluidHandler;
import openmods.sync.SyncableBoolean;
import openmods.sync.SyncableSides;
import openmods.sync.drops.StoreOnDrop;
import openmods.utils.SidedInventoryAdapter;

public class TileEntityElectrolyzer extends TileEntityMachineBase implements IFluidHandler {
    private SyncableBoolean active;

    @StoreOnDrop(name = ItemMachine.ENERGY_STORAGE_TAG)
    protected SyncableEnergyStorage energyStorage;
    protected GenericInventory inventory;

    private MonitoredTank inputTank;
    private MonitoredTank outputTank1;
    private MonitoredTank outputTank2;

    private SyncableSides batteryDirections;
    private SyncableSides fluidCanister1Input;
    private SyncableSides fluidCanister2Input;

    private SyncableSides inputDirections;
    private SyncableSides output1Directions;
    private SyncableSides output2Directions;

    @IncludeInterface(ISidedInventory.class)
    private SidedInventoryAdapter sidedInventory = new SidedInventoryAdapter(inventory);

    private IFluidHandler inputTankWrapper = new SidedFluidHandler.Drain(inputDirections, inputTank);
    private IFluidHandler outputTank1Wrapper = new SidedFluidHandler.Source(output1Directions, outputTank1);
    private IFluidHandler outputTank2Wrapper = new SidedFluidHandler.Source(output2Directions, outputTank2);

    public TileEntityElectrolyzer() {
        sidedInventory.registerSlot(0, batteryDirections, true, true);
        sidedInventory.registerSlot(1, fluidCanister1Input, true, true);
        sidedInventory.registerSlot(2, fluidCanister2Input, true, true);
    }

    @Override
    protected void createSyncedFields() {
        active = new SyncableBoolean();
        energyStorage = new SyncableEnergyStorage(Config.EnergyStorage.ELECTROLYZER);
        inventory = registerInventoryCallback(new TileEntityInventory(this, "electrolyzer", false, 3));

        inputTank = new MonitoredTank(Config.FluidStorage.ELECTROLYZER_IN);
        outputTank1 = new MonitoredTank(Config.FluidStorage.ELECTROLYZER_OUT);
        outputTank2 = new MonitoredTank(Config.FluidStorage.ELECTROLYZER_OUT);

        batteryDirections = new SyncableSides();
        fluidCanister1Input = new SyncableSides();
        fluidCanister2Input = new SyncableSides();

        inputDirections = new SyncableSides();
        output1Directions = new SyncableSides();
        output2Directions = new SyncableSides();

        for(int i = 0; i < 6; i++) {
            batteryDirections.set(ForgeDirection.getOrientation(i), true);
            fluidCanister1Input.set(ForgeDirection.getOrientation(i), true);
            fluidCanister2Input.set(ForgeDirection.getOrientation(i), true);
            inputDirections.set(ForgeDirection.getOrientation(i), true);
        }

        output1Directions.set(ForgeDirection.UP, true);
        output2Directions.set(ForgeDirection.DOWN, true);
    }

    public boolean isActive() {
        return active.get();
    }
    public void setActive(boolean active) {
        this.active.set(active);
    }

    public SyncableEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public IInventory getInventory() {
        return inventory;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return inputTankWrapper.fill(from, resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if(resource == null) return null;
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        int drained1 = outputTank1Wrapper.drain(from, maxDrain, false) != null ? outputTank1Wrapper.drain(from, maxDrain, false).amount : 0;
        int drained2 = outputTank2Wrapper.drain(from, maxDrain, false) != null ? outputTank2Wrapper.drain(from, maxDrain, false).amount : 0;

        return drained1 > drained2 ? outputTank1Wrapper.drain(from, maxDrain, doDrain) : outputTank2Wrapper.drain(from, maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return inputTankWrapper.canFill(from, fluid);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return outputTank1Wrapper.canDrain(from, fluid) || outputTank2Wrapper.canDrain(from, fluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        FluidTankInfo[] tankInfos = new FluidTankInfo[3];
        tankInfos[0] = inputTank.getInfo();
        tankInfos[1] = outputTank1.getInfo();
        tankInfos[2] = outputTank2.getInfo();
        return tankInfos;
    }

    public MonitoredTank getInputTank() {
        return inputTank;
    }

    public MonitoredTank getOutputTank1() {
        return outputTank1;
    }

    public MonitoredTank getOutputTank2() {
        return outputTank2;
    }
}
