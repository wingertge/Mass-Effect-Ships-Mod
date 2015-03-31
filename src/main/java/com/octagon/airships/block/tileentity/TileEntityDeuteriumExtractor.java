package com.octagon.airships.block.tileentity;

import com.octagon.airships.block.item.ItemDeuteriumExtractor;
import com.octagon.airships.block.item.ItemMachine;
import com.octagon.airships.client.gui.machine.ContainerDeuteriumExtractor;
import com.octagon.airships.client.gui.machine.GuiDeuteriumExtractor;
import com.octagon.airships.init.ModFluids;
import com.octagon.airships.reference.Config;
import com.octagon.airships.sync.MonitoredInt;
import com.octagon.airships.sync.MonitoredTank;
import com.octagon.airships.sync.SyncableEnergyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import openmods.api.IHasGui;
import openmods.inventory.GenericInventory;
import openmods.liquids.SidedFluidHandler;
import openmods.sync.SyncableBoolean;
import openmods.sync.SyncableSides;
import openmods.sync.drops.StoreOnDrop;

import java.util.Random;

public class TileEntityDeuteriumExtractor extends TileEntityMachineBase implements IFluidHandler, IHasGui {
    private Random random = new Random(12307352345L);
    private SyncableBoolean active;

    @StoreOnDrop(name = ItemMachine.ENERGY_STORAGE_TAG)
    protected SyncableEnergyStorage energyStorage;

    @StoreOnDrop
    private MonitoredInt currentWork;
    private MonitoredInt maxWork;

    private SyncableSides inputDirections;
    private SyncableSides outputDirections;

    @StoreOnDrop(name = ItemDeuteriumExtractor.INPUT_TANK_TAG)
    private MonitoredTank inputTank;

    @StoreOnDrop(name = ItemDeuteriumExtractor.OUTPUT_TANK_TAG)
    private MonitoredTank outputTank;

    private GenericInventory inventory = new GenericInventory("deuteriumExtractor", false, 3);

    private IFluidHandler inputTankWrapper = new SidedFluidHandler.Drain(inputDirections, inputTank);
    private IFluidHandler outputTankWrapper = new SidedFluidHandler.Source(outputDirections, outputTank);

    @Override
    protected void createSyncedFields() {
        active = new SyncableBoolean(false);
        energyStorage = new SyncableEnergyStorage(Config.EnergyStorage.MACHINE_GENERIC);

        currentWork = new MonitoredInt(0);
        maxWork = new MonitoredInt(Config.Work.DEUTERIUM);

        inputDirections = new SyncableSides();
        outputDirections = new SyncableSides();

        inputTank = new MonitoredTank(Config.FluidStorage.DEUTERIUM_EXTRACTOR_IN, new FluidStack(FluidRegistry.WATER, 1));
        outputTank = new MonitoredTank(Config.FluidStorage.DEUTERIUM_EXTRACTOR_OUT, new FluidStack(ModFluids.liquidDeuterium, 1));

        for(int i = 0; i < 6; i++) {
            inputDirections.set(ForgeDirection.getOrientation(i), true);
        }
        outputDirections.set(ForgeDirection.UP, true);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if((inputTank.isDirty() || outputTank.isDirty()) && !worldObj.isRemote) sync();

        if(currentWork.get() < maxWork.get() && getEnergyStorage().getEnergyStored() >= Config.EnergyUsage.DEUTERIUM_EXTRACTOR) {
            currentWork.set(currentWork.get() + 1);
            getEnergyStorage().extractEnergy(Config.EnergyUsage.DEUTERIUM_EXTRACTOR, true);
        }

        if(currentWork.get() >= maxWork.get()) {
            FluidStack stack = new FluidStack(ModFluids.liquidDeuterium, 15 + random.nextInt(2));
            if(outputTank.fill(stack, false) == stack.amount && inputTank.drain(1000, false) != null &&
                    inputTank.drain(1000, false).amount == 1000) {
                outputTank.fill(stack, true);
                inputTank.drain(1000, true);
                currentWork.set(0);
            }
        }

        outputTank.distributeToSides(50, worldObj, getPosition(), outputDirections.getValue());
    }

    @Override
    public boolean isActive() {
        return active.get();
    }

    @Override
    public void setActive(boolean active) {
        this.active.set(active);
    }

    @Override
    public SyncableEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return inputTankWrapper.fill(from, resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return outputTankWrapper.drain(from, resource, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return outputTankWrapper.drain(from, maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return inputTankWrapper.canFill(from, fluid);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return outputTankWrapper.canDrain(from, fluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        FluidTankInfo[] tankInfos = new FluidTankInfo[2];
        tankInfos[0] = inputTank.getInfo();
        tankInfos[1] = outputTank.getInfo();
        return tankInfos;
    }

    @Override
    public Object getServerGui(EntityPlayer player) {
        return new ContainerDeuteriumExtractor(player.inventory, this);
    }

    @Override
    public Object getClientGui(EntityPlayer player) {
        return new GuiDeuteriumExtractor(new ContainerDeuteriumExtractor(player.inventory, this));
    }

    @Override
    public boolean canOpenGui(EntityPlayer player) {
        return true;
    }

    @Override
    public IInventory getInventory() {
        return inventory;
    }

    public MonitoredInt getCurrentWork() {
        return currentWork;
    }

    public MonitoredInt getMaxWork() {
        return maxWork;
    }

    public MonitoredTank getInputTank() {
        return inputTank;
    }

    public MonitoredTank getOutputTank() {
        return outputTank;
    }
}
