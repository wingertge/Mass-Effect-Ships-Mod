package com.octagon.airships.block.tileentity;

import com.octagon.airships.block.item.ItemLaunchPad;
import com.octagon.airships.block.multiblock.MultiblockStructure;
import com.octagon.airships.init.ModFluids;
import com.octagon.airships.reference.Config;
import com.octagon.airships.sync.MonitoredInt;
import com.octagon.airships.sync.MonitoredTank;
import com.octagon.airships.sync.SyncableCoord;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import openmods.include.IncludeInterface;
import openmods.inventory.GenericInventory;
import openmods.inventory.IInventoryProvider;
import openmods.inventory.TileEntityInventory;
import openmods.liquids.SidedFluidHandler;
import openmods.sync.SyncableSides;
import openmods.sync.drops.StoreOnDrop;
import openmods.utils.SidedInventoryAdapter;
import openmods.utils.bitmap.BitMapUtils;
import openmods.utils.bitmap.IReadableBitMap;
import openmods.utils.bitmap.IRpcDirectionBitMap;
import openmods.utils.bitmap.IWriteableBitMap;

import java.util.Random;

public class TileEntityLaunchPad extends TileEntityMultiblockPart implements IInventoryProvider, IFluidHandler {
    private Random random = new Random(1234873062524382341L);

    private MultiblockStructure structure;
    private SyncableCoord base;

    private final GenericInventory inventory = registerInventoryCallback(new TileEntityInventory(this, "launchPad", true, 1));
    @StoreOnDrop(name = ItemLaunchPad.TANK_1_TAG)
    private MonitoredTank tank1;
    @StoreOnDrop(name = ItemLaunchPad.TANK_2_TAG)
    private MonitoredTank tank2;
    private SyncableSides fluid1Inputs;
    private SyncableSides fluid2Inputs;
    private SyncableSides probesInputs;

    @StoreOnDrop(name = ItemLaunchPad.CURRENT_WORK_TAG)
    private MonitoredInt currentWork;
    private MonitoredInt maxWork;

    private IFluidHandler tank1Wrapper = new SidedFluidHandler.Drain(fluid1Inputs, tank1);
    private IFluidHandler tank2Wrapper = new SidedFluidHandler.Drain(fluid2Inputs, tank2);

    @IncludeInterface(ISidedInventory.class)
    private SidedInventoryAdapter sidedInventory = new SidedInventoryAdapter(inventory);

    public TileEntityLaunchPad() {
        sidedInventory.registerSlot(0, probesInputs, true, true);
    }

    @Override
    protected void createSyncedFields() {
        base = new SyncableCoord();
        structure = new MultiblockStructure(base);

        tank1 = new MonitoredTank(Config.FluidStorage.LAUNCH_PAD, new FluidStack(ModFluids.liquidHydrogen, 1), new FluidStack(ModFluids.liquidHelium3, 1));
        tank2 = new MonitoredTank(Config.FluidStorage.LAUNCH_PAD, new FluidStack(ModFluids.liquidDeuterium, 1), new FluidStack(ModFluids.antiprotons, 1), new FluidStack(ModFluids.liquidOxygen, 1));

        fluid1Inputs = new SyncableSides();
        fluid2Inputs = new SyncableSides();
        probesInputs = new SyncableSides();

        currentWork = new MonitoredInt();
        maxWork = new MonitoredInt(600);

        for(int i = 0; i < 6; i++) {
            if(i == 1) continue;
            fluid1Inputs.set(ForgeDirection.getOrientation(i), true);
            fluid2Inputs.set(ForgeDirection.getOrientation(i), true);
            probesInputs.set(ForgeDirection.getOrientation(i), true);
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if((tank1.isDirty() || tank2.isDirty()) && !worldObj.isRemote) sync();

        if(currentWork.get() < maxWork.get()) {
            TileEntity tileEntity = worldObj.getTileEntity(base.getX(), base.getY(), base.getZ());
            if(tileEntity != null && tileEntity instanceof TileEntityLaunchPadMultiblock) {
                if(((TileEntityLaunchPadMultiblock) tileEntity).extractEnergy(Config.EnergyUsage.LAUNCH_PAD))
                    currentWork.set(currentWork.get() + 1);
            }
        }
    }

    public MultiblockStructure getStructure() {
        return structure;
    }

    public void setStructure(MultiblockStructure structure) {
        this.structure.assign(structure);
        if(!worldObj.isRemote) sync();
    }

    public SyncableCoord getBase() {
        return base;
    }

    public void setBase(SyncableCoord base) {
        this.base.assign(base);
        if(!worldObj.isRemote) sync();
    }

    @Override
    public IInventory getInventory() {
        return inventory;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        inventory.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        inventory.writeToNBT(tag);
    }

    public IReadableBitMap<ForgeDirection> getReadableFluid1Inputs() {
        return fluid1Inputs;
    }

    public IWriteableBitMap<ForgeDirection> getWritableFluid1Inputs() {
        return BitMapUtils.createRpcAdapter(createRpcProxy(fluid1Inputs, IRpcDirectionBitMap.class));
    }

    public IReadableBitMap<ForgeDirection> getReadableFluid2Inputs() {
        return fluid2Inputs;
    }

    public IWriteableBitMap<ForgeDirection> getWritableFluid2Inputs() {
        return BitMapUtils.createRpcAdapter(createRpcProxy(fluid2Inputs, IRpcDirectionBitMap.class));
    }

    public IReadableBitMap<ForgeDirection> getReadableProbeInputs() {
        return probesInputs;
    }

    public IWriteableBitMap<ForgeDirection> getWritableProbeInputs() {
        return BitMapUtils.createRpcAdapter(createRpcProxy(probesInputs, IRpcDirectionBitMap.class));
    }

    public MonitoredTank getTank(int id) {
        return id == 1 ? tank1 : tank2;
    }

    public int getCurrentWork() {
        return currentWork.get();
    }

    public int getMaxWork() {
        return maxWork.get();
    }

    public void setCurrentWork(int currentWork) {
        this.currentWork.set(currentWork);
    }

    public FluidStack getFluidResult() {
        int amount = random.nextInt(251) + 250;
        return new FluidStack(ModFluids.liquidHelium3, amount);
    }

    public ItemStack getItemResult() {
        return null;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        int filled1 = tank1.fill(resource, false);
        int filled2 = tank2.fill(resource, false);
        if(filled1 > filled2)
            return tank1.fill(resource, doFill);
        else return tank2.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        int drained1 = tank1.drain(maxDrain, false) != null ? tank1.drain(maxDrain, false).amount : 0;
        int drained2 = tank2.drain(maxDrain, false) != null ? tank2.drain(maxDrain, false).amount : 0;
        if(drained1 > drained2)
            return tank1.drain(maxDrain, doDrain);
        else return tank2.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return tank1.fill(new FluidStack(fluid, 1), false) > 0 || tank2.fill(new FluidStack(fluid, 1), false) > 0;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return tank1.drain(new FluidStack(fluid, 1), false) != null || tank2.drain(new FluidStack(fluid, 1), false) != null;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        FluidTankInfo[] infos = new FluidTankInfo[2];
        infos[0] = tank1.getInfo();
        infos[1] = tank2.getInfo();
        return infos;
    }
}
