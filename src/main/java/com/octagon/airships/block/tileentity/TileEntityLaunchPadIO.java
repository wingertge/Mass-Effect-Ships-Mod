package com.octagon.airships.block.tileentity;

import com.octagon.airships.block.multiblock.MultiblockStructure;
import com.octagon.airships.reference.Config;
import com.octagon.airships.sync.MonitoredTank;
import com.octagon.airships.sync.SyncableCoord;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;
import openmods.include.IncludeInterface;
import openmods.inventory.GenericInventory;
import openmods.inventory.IInventoryProvider;
import openmods.inventory.TileEntityInventory;
import openmods.liquids.SidedFluidHandler;
import openmods.sync.SyncableSides;
import openmods.utils.SidedInventoryAdapter;
import openmods.utils.bitmap.BitMapUtils;
import openmods.utils.bitmap.IReadableBitMap;
import openmods.utils.bitmap.IRpcDirectionBitMap;
import openmods.utils.bitmap.IWriteableBitMap;

public class TileEntityLaunchPadIO extends TileEntityMultiblockPart implements IInventoryProvider {
    private MultiblockStructure structure;
    private SyncableCoord base;

    private MonitoredTank outputTank;
    private GenericInventory inventory = registerInventoryCallback(new TileEntityInventory(this, "launchPadIO", false, 3));
    private SyncableSides itemOutputs;
    private SyncableSides fluidOutputs;
    private SyncableSides fuelCanisterInputs;
    private SyncableSides outputFluidContainerOutputs;

    @IncludeInterface(ISidedInventory.class)
    private SidedInventoryAdapter sidedInventory = new SidedInventoryAdapter(inventory);

    @IncludeInterface
    private IFluidHandler tankWrapper = new SidedFluidHandler.Source(fluidOutputs, outputTank);

    public TileEntityLaunchPadIO() {
        sidedInventory.registerSlot(0, fuelCanisterInputs, true, true);
        sidedInventory.registerSlot(1, itemOutputs, false, true);
        sidedInventory.registerSlot(2, outputFluidContainerOutputs, true, true);
    }

    @Override
    protected void createSyncedFields() {
        base = new SyncableCoord();
        structure = new MultiblockStructure(base);
        outputTank = new MonitoredTank(Config.FluidStorage.LAUNCH_PAD_OUTPUT);

        itemOutputs = new SyncableSides();
        fluidOutputs = new SyncableSides();
        fuelCanisterInputs = new SyncableSides();
        outputFluidContainerOutputs = new SyncableSides();

        for(int i = 0; i < 6; i++) {
            if(i == 1) continue;
            itemOutputs.set(ForgeDirection.getOrientation(i), true);
            fluidOutputs.set(ForgeDirection.getOrientation(i), true);
            fuelCanisterInputs.set(ForgeDirection.getOrientation(i), true);
            outputFluidContainerOutputs.set(ForgeDirection.getOrientation(i), true);
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        outputTank.distributeToSides(50, worldObj, getPosition(), fluidOutputs.getValue());
    }

    public MultiblockStructure getStructure() {
        return structure;
    }

    public void setStructure(MultiblockStructure structure) {
        this.getStructure().assign(structure);
        if(!getWorldObj().isRemote) sync();
    }

    public SyncableCoord getBase() {
        return base;
    }

    public void setBase(SyncableCoord base) {
        this.getBase().assign(base);
        if(!getWorldObj().isRemote) sync();
    }

    @Override
    public IInventory getInventory() {
        return inventory;
    }

    public MonitoredTank getOutputTank() {
        return outputTank;
    }

    public IReadableBitMap<ForgeDirection> getReadableFuelCanisterInputs() {
        return fuelCanisterInputs;
    }

    public IWriteableBitMap<ForgeDirection> getWritableFuelCanisterInputs() {
        return BitMapUtils.createRpcAdapter(createRpcProxy(fuelCanisterInputs, IRpcDirectionBitMap.class));
    }

    public IReadableBitMap<ForgeDirection> getReadableItemOutputs() {
        return itemOutputs;
    }

    public IWriteableBitMap<ForgeDirection> getWritableItemOutputs() {
        return BitMapUtils.createRpcAdapter(createRpcProxy(itemOutputs, IRpcDirectionBitMap.class));
    }

    public IReadableBitMap<ForgeDirection> getReadableFluidOutputs() {
        return fluidOutputs;
    }

    public IWriteableBitMap<ForgeDirection> getWritableFluidOutputs() {
        return BitMapUtils.createRpcAdapter(createRpcProxy(fluidOutputs, IRpcDirectionBitMap.class));
    }

    public IReadableBitMap<ForgeDirection> getReadableFluidCanisterOutput() {
        return outputFluidContainerOutputs;
    }

    public IWriteableBitMap<ForgeDirection> getWritableFluidCanisterOutput() {
        return BitMapUtils.createRpcAdapter(createRpcProxy(outputFluidContainerOutputs, IRpcDirectionBitMap.class));
    }
}
