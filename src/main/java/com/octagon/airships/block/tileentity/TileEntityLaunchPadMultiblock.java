package com.octagon.airships.block.tileentity;

import com.google.common.collect.Sets;
import com.octagon.airships.block.BlockLaunchControllerExtension;
import com.octagon.airships.block.BlockLaunchPad;
import com.octagon.airships.block.BlockLaunchPadController;
import com.octagon.airships.block.multiblock.MultiblockStructure;
import com.octagon.airships.block.multiblock.TileEntityMultiblock;
import com.octagon.airships.client.gui.multiblock.ContainerLaunchPadMultiblock;
import com.octagon.airships.client.gui.multiblock.GuiLaunchPadMultiblock;
import com.octagon.airships.init.ModFluids;
import com.octagon.airships.reference.Config;
import com.octagon.airships.sync.SyncableCoord;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import openmods.api.IHasGui;
import openmods.api.IValueProvider;
import openmods.include.IncludeInterface;
import openmods.inventory.GenericInventory;
import openmods.inventory.IInventoryProvider;
import openmods.inventory.TileEntityInventory;
import openmods.liquids.SidedFluidHandler;
import openmods.sync.*;
import openmods.utils.BlockNotifyFlags;
import openmods.utils.SidedInventoryAdapter;
import openmods.utils.bitmap.BitMapUtils;
import openmods.utils.bitmap.IReadableBitMap;
import openmods.utils.bitmap.IRpcDirectionBitMap;
import openmods.utils.bitmap.IWriteableBitMap;

import java.util.HashSet;
import java.util.Set;

public class TileEntityLaunchPadMultiblock extends TileEntityMultiblock implements IInventoryProvider, IHasGui, ISyncListener, INeighborPlacedAware {
    private MultiblockStructure structure;
    private int rebuildTimer;
    private Set<SyncableCoord> controllers;
    private Set<SyncableCoord> launchPads;

    private SyncableTank tank1;
    private SyncableTank tank2;
    private SyncableSides fluid1Inputs;
    private SyncableSides fluid2Inputs;
    private SyncableSides probesInputs;
    private SyncableSides fuelCanisterInputs;
    private SyncableSides itemOutputs;

    private GenericInventory inventory = registerInventoryCallback(new TileEntityInventory(this, "launchPad", true, 7));
    private SyncableInt launchPadCount;
    private int lastLaunchPadCount = 1;

    @IncludeInterface(ISidedInventory.class)
    private SidedInventoryAdapter sided = new SidedInventoryAdapter(inventory);

    @IncludeInterface
    private final IFluidHandler tankWrapper1 = new SidedFluidHandler.Drain(fluid1Inputs, tank1);

    private final IFluidHandler tankWrapper2 = new SidedFluidHandler.Drain(fluid2Inputs, tank2);

    public TileEntityLaunchPadMultiblock() {
        rebuildTimer = 2;

        sided.registerSlot(1, fuelCanisterInputs, true, true);
        sided.registerSlots(2, 4, itemOutputs, false, true);
        sided.registerSlots(6, inventory.getSizeInventory() - 6, probesInputs, true, true);

        syncMap.addSyncListener(this);
    }

    @Override
    protected void createSyncedFields() {
        launchPadCount = new SyncableInt(1);

        tank1 = new SyncableTank(Config.FluidStorage.LAUNCH_PAD, new FluidStack(ModFluids.liquidHydrogen, 1), new FluidStack(ModFluids.liquidHelium3, 1));
        tank2 = new SyncableTank(Config.FluidStorage.LAUNCH_PAD, new FluidStack(ModFluids.liquidDeuterium, 1), new FluidStack(ModFluids.antiprotons, 1), new FluidStack(ModFluids.liquidOxygen, 1));
        fluid1Inputs = new SyncableSides();
        fluid2Inputs = new SyncableSides();
        probesInputs = new SyncableSides();
        fuelCanisterInputs = new SyncableSides();
        itemOutputs = new SyncableSides();
        structure = new MultiblockStructure(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));
        controllers = Sets.newHashSet();
        launchPads = Sets.newHashSet();

        for(int i = 0; i < 4; i++) {
            fluid1Inputs.set(ForgeDirection.getOrientation(i + 2), true);
            fluid2Inputs.set(ForgeDirection.getOrientation(i + 2), true);
            probesInputs.set(ForgeDirection.getOrientation(i + 2), true);
            itemOutputs.set(ForgeDirection.getOrientation(i + 2), true);
        }
        fuelCanisterInputs.set(ForgeDirection.DOWN, true);
    }

    @Override
    public SyncableCoord getBaseBlock() {
        return new SyncableCoord(worldObj, xCoord, yCoord, zCoord);
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public void rebuild() {
        if(worldObj.isRemote) return;

        breakStructure();
        assembleRecursive(controllers, launchPads, Sets.newHashSet(), xCoord, yCoord, zCoord);
        if(launchPads.size() >= 1) completeStructure();
    }

    private void breakStructure() {
        structure.setComplete(false);

        structure.getBlocks().forEach(a -> {
            TileEntity tileEntity = worldObj.getTileEntity(a.getX(), a.getY(), a.getZ());
            if(tileEntity != null && tileEntity instanceof TileEntityMultiblockPart) {
                ((TileEntityMultiblockPart)tileEntity).setBase(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));
                ((TileEntityMultiblockPart)tileEntity).setStructure(structure);
            }
            worldObj.setBlockMetadataWithNotify(a.getX(), a.getY(), a.getZ(), 0, BlockNotifyFlags.ALL);
        });

        structure.clearBlocks();
        structure.setBase(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, BlockNotifyFlags.ALL);
        controllers.clear();
        launchPads.clear();

        if(!worldObj.isRemote) sync();
    }

    private void completeStructure() {
        structure.setBase(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));
        structure.clearBlocks();

        Set<SyncableCoord> blocks = Sets.newHashSet();
        blocks.addAll(controllers);
        blocks.addAll(launchPads);
        structure.setBlocks(blocks);
        structure.setComplete(true);

        structure.getBlocks().stream().forEach(a -> {
            TileEntity tileEntity = worldObj.getTileEntity(a.getX(), a.getY(), a.getZ());
            if(tileEntity != null && tileEntity instanceof TileEntityMultiblockPart) {
                TileEntityMultiblockPart part = (TileEntityMultiblockPart)tileEntity;
                part.setBase(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));
                part.setStructure(structure);
            }
            worldObj.setBlockMetadataWithNotify(a.getX(), a.getY(), a.getZ(), 1, BlockNotifyFlags.ALL);
            worldObj.markBlockForUpdate(a.getX(), a.getY(), a.getZ());
        });

        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, BlockNotifyFlags.ALL);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

        launchPadCount.set(launchPads.size());

        if(!worldObj.isRemote) sync();
    }

    @Override
    public void queueStructureValidation() {
        if(!worldObj.isRemote)
            rebuildTimer = 1;
    }

    @Override
    public void updateEntity() {
        if(rebuildTimer != 0) {
            rebuildTimer--;
            if(rebuildTimer == 0) rebuild();
        }

        if(lastLaunchPadCount != launchPadCount.get()) {
            NBTTagCompound compound = new NBTTagCompound();
            inventory.writeToNBT(compound);
            inventory.clearAndSetSlotCount(6 + launchPadCount.get());
            compound.setInteger("size", 6 + launchPadCount.get());
            inventory.readFromNBT(compound);

            lastLaunchPadCount = launchPadCount.get();
        }

        super.updateEntity();
    }

    @Override
    public MultiblockStructure getStructure() {
        return structure;
    }

    @Override
    public void setStructure(MultiblockStructure structure) {
        this.structure.assign(structure);
    }

    private void assembleRecursive(Set<SyncableCoord> controllers, Set<SyncableCoord> launchPads, HashSet<ChunkPosition> set, int x, int y, int z) {
        ChunkPosition pos = new ChunkPosition(x, y, z);
        if (set.contains(pos)) return;

        set.add(pos);
        Block block = worldObj.getBlock(x, y, z);
        if (!(block instanceof BlockLaunchControllerExtension || block instanceof BlockLaunchPadController || block instanceof BlockLaunchPad)) return;

        SyncableCoord coord = new SyncableCoord(worldObj, x, y, z);
        if(block instanceof BlockLaunchPad)
            launchPads.add(coord);
        else
            controllers.add(coord);

        TileEntity tileEntity = worldObj.getTileEntity(x, y, z);
        if(tileEntity != null && tileEntity instanceof TileEntityMultiblockPart) {
            ((TileEntityMultiblockPart) tileEntity).setBase(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));
        }

        assembleRecursive(controllers, launchPads, set, x - 1, y, z);
        assembleRecursive(controllers, launchPads, set, x, y - 1, z);
        assembleRecursive(controllers, launchPads, set, x, y, z - 1);
        assembleRecursive(controllers, launchPads, set, x + 1, y, z);
        assembleRecursive(controllers, launchPads, set, x, y + 1, z);
        assembleRecursive(controllers, launchPads, set, x, y, z + 1);
    }

    @Override
    public IInventory getInventory() {
        return inventory;
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

    public IReadableBitMap<ForgeDirection> getReadableProbeInputs() {
        return probesInputs;
    }

    public IWriteableBitMap<ForgeDirection> getWritableProbeInputs() {
        return BitMapUtils.createRpcAdapter(createRpcProxy(probesInputs, IRpcDirectionBitMap.class));
    }

    public IValueProvider<FluidStack> getFluidProvider1() {
        return tank1;
    }

    public IValueProvider<FluidStack> getFluidProvider2() {
        return tank2;
    }

    @Override
    public Object getServerGui(EntityPlayer player) {
        return new ContainerLaunchPadMultiblock(player.inventory, this);
    }

    @Override
    public Object getClientGui(EntityPlayer player) {
        return new GuiLaunchPadMultiblock(new ContainerLaunchPadMultiblock(player.inventory, this));
    }

    @Override
    public boolean canOpenGui(EntityPlayer player) {
        return blockMetadata == 1;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        inventory.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        inventory.readFromNBT(tag);
    }

    @Override
    public void onSync(Set<ISyncableObject> changes) {
    }

    @Override
    public void neighborPlaced(Block block) {
        rebuild();
    }
}
