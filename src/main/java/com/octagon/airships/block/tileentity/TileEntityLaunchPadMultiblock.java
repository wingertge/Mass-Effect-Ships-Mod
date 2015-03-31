package com.octagon.airships.block.tileentity;

import cofh.api.energy.IEnergyReceiver;
import com.google.common.collect.Sets;
import com.octagon.airships.block.BlockLaunchControllerExtension;
import com.octagon.airships.block.BlockLaunchPad;
import com.octagon.airships.block.BlockLaunchPadController;
import com.octagon.airships.block.BlockLaunchPadIO;
import com.octagon.airships.block.fluid.LaunchPadFluidTank;
import com.octagon.airships.block.inventory.LaunchPadInventory;
import com.octagon.airships.block.item.ItemLaunchPadController;
import com.octagon.airships.block.multiblock.MultiblockStructure;
import com.octagon.airships.block.multiblock.TileEntityMultiblock;
import com.octagon.airships.client.gui.multiblock.ContainerLaunchPadMultiblock;
import com.octagon.airships.client.gui.multiblock.GuiLaunchPadMultiblock;
import com.octagon.airships.reference.Config;
import com.octagon.airships.sync.MonitoredTank;
import com.octagon.airships.sync.SyncableCoord;
import com.octagon.airships.sync.SyncableEnergyStorage;
import com.octagon.airships.sync.SyncableSet;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import openmods.api.IHasGui;
import openmods.include.IncludeInterface;
import openmods.inventory.GenericInventory;
import openmods.inventory.IInventoryProvider;
import openmods.sync.ISyncListener;
import openmods.sync.ISyncableObject;
import openmods.sync.SyncableSides;
import openmods.sync.drops.StoreOnDrop;
import openmods.utils.BlockNotifyFlags;
import openmods.utils.SidedInventoryAdapter;
import openmods.utils.bitmap.BitMapUtils;
import openmods.utils.bitmap.IReadableBitMap;
import openmods.utils.bitmap.IRpcDirectionBitMap;
import openmods.utils.bitmap.IWriteableBitMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TileEntityLaunchPadMultiblock extends TileEntityMultiblock implements IInventoryProvider, IHasGui, ISyncListener, INeighborPlacedAware, IEnergyReceiver {
    private MultiblockStructure structure;
    private int rebuildTimer;
    private SyncableSet<SyncableCoord> controllers;
    private SyncableSet<SyncableCoord> launchPads;
    private SyncableCoord io;
    private GenericInventory coreInventory = registerInventoryCallback(new GenericInventory("launchPadCore", false, 1));

    @StoreOnDrop(name = ItemLaunchPadController.ENERGY_STORAGE_TAG)
    private SyncableEnergyStorage energyStorage;

    private LaunchPadInventory inventory = new LaunchPadInventory(new ArrayList<>(), this);
    private LaunchPadFluidTank ghostTank1;
    private LaunchPadFluidTank ghostTank2;

    private SyncableSides batteryInputs;

    @IncludeInterface(ISidedInventory.class)
    private SidedInventoryAdapter sidedInventory = new SidedInventoryAdapter(coreInventory);

    private int lastSize = 1;

    public TileEntityLaunchPadMultiblock() {
        rebuildTimer = 2;

        syncMap.addSyncListener(this);
        sidedInventory.registerSlot(0, batteryInputs, true, true);
    }

    @Override
    protected void createSyncedFields() {
        energyStorage = new SyncableEnergyStorage(Config.EnergyStorage.LAUNCH_PAD_CONTROLLER);
        io = new SyncableCoord();
        structure = new MultiblockStructure(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));
        controllers = new SyncableSet<>(SyncableCoord.class);
        launchPads = new SyncableSet<>(SyncableCoord.class);

        batteryInputs = new SyncableSides();

        for(int i = 0; i < 6; i++) {
            batteryInputs.set(ForgeDirection.getOrientation(i), true);
        }

        ghostTank1 = new LaunchPadFluidTank(1, new ArrayList<>());
        ghostTank2 = new LaunchPadFluidTank(2, new ArrayList<>());
    }

    @Override
    public SyncableCoord getBaseBlock() {
        return new SyncableCoord(worldObj, xCoord, yCoord, zCoord);
    }

    @Override
    public boolean isComplete() {
        return structure.isComplete();
    }

    @Override
    public void rebuild() {
        if(worldObj.isRemote) return;

        breakStructure();
        assembleRecursive(controllers, launchPads, io, Sets.newHashSet(), xCoord, yCoord, zCoord);
        if(launchPads.size() >= 1 && worldObj.getTileEntity(io.getX(), io.getY(), io.getZ()) != null && worldObj.getTileEntity(io.getX(), io.getY(), io.getZ()) instanceof TileEntityLaunchPadIO)
            completeStructure();
    }

    private void breakStructure() {
        structure.setComplete(false);

        structure.getBlocks().forEach(a -> {
            TileEntity tileEntity = worldObj.getTileEntity(a.getX(), a.getY(), a.getZ());
            if (tileEntity != null && tileEntity instanceof TileEntityMultiblockPart) {
                ((TileEntityMultiblockPart) tileEntity).setBase(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));
                ((TileEntityMultiblockPart) tileEntity).setStructure(structure);
            }
            worldObj.setBlockMetadataWithNotify(a.getX(), a.getY(), a.getZ(), 0, BlockNotifyFlags.ALL);
        });

        structure.clearBlocks();
        structure.setBase(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, BlockNotifyFlags.ALL);
        controllers.clear();
        launchPads.clear();
        io.assign(new SyncableCoord());

        if(!worldObj.isRemote) sync();
    }

    private void completeStructure() {
        structure.setBase(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));
        structure.clearBlocks();

        Set<SyncableCoord> blocks = Sets.newHashSet();
        blocks.addAll(controllers);
        blocks.addAll(launchPads);
        blocks.add(io);
        structure.setBlocks(blocks);
        structure.setComplete(true);

        structure.getBlocks().stream().forEach(a -> {
            TileEntity tileEntity = worldObj.getTileEntity(a.getX(), a.getY(), a.getZ());
            if (tileEntity != null && tileEntity instanceof TileEntityMultiblockPart) {
                TileEntityMultiblockPart part = (TileEntityMultiblockPart) tileEntity;
                part.setBase(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));
                part.setStructure(structure);
            }
            worldObj.setBlockMetadataWithNotify(a.getX(), a.getY(), a.getZ(), 1, BlockNotifyFlags.ALL);
            worldObj.markBlockForUpdate(a.getX(), a.getY(), a.getZ());
        });

        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, BlockNotifyFlags.ALL);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

        List<TileEntityLaunchPad> tileEntities = launchPads.parallelStream().filter(a -> worldObj.getTileEntity(a.getX(), a.getY(), a.getZ()) != null &&
                worldObj.getTileEntity(a.getX(), a.getY(), a.getZ()) instanceof TileEntityLaunchPad)
                .map(a -> (TileEntityLaunchPad) worldObj.getTileEntity(a.getX(), a.getY(), a.getZ())).collect(Collectors.toList());
        inventory.setLaunchPads(tileEntities);
        ghostTank1.setLaunchPads(tileEntities);
        ghostTank2.setLaunchPads(tileEntities);

        if(!worldObj.isRemote) sync();
    }

    @Override
    public void queueStructureValidation() {
        if(!worldObj.isRemote)
            rebuildTimer = 1;
    }

    @Override
    public void updateEntity() {
        if(!worldObj.isRemote) sync();

        if(rebuildTimer != 0) {
            rebuildTimer--;
            if(rebuildTimer == 0) rebuild();
        }

        List<TileEntityLaunchPad> tileEntities = launchPads.parallelStream().filter(a -> worldObj.getTileEntity(a.getX(), a.getY(), a.getZ()) != null &&
                worldObj.getTileEntity(a.getX(), a.getY(), a.getZ()) instanceof TileEntityLaunchPad)
                .map(a -> (TileEntityLaunchPad) worldObj.getTileEntity(a.getX(), a.getY(), a.getZ())).collect(Collectors.toList());

        if(lastSize != launchPads.size() && isComplete()) {
            inventory.setLaunchPads(tileEntities);
            inventory.setIOBlock((TileEntityLaunchPadIO) worldObj.getTileEntity(io.getX(), io.getY(), io.getZ()));
            ghostTank1.setLaunchPads(tileEntities);
            ghostTank2.setLaunchPads(tileEntities);
            lastSize = launchPads.size();
        }

        if(isComplete()) {
            tileEntities.stream().filter(a -> a.getCurrentWork() >= a.getMaxWork()).forEach(a -> {
                ItemStack itemResult = a.getItemResult();
                FluidStack fluidResult = a.getFluidResult();
                if (itemResult != null) {
                    if (coreInventory.getStackInSlot(3) == null) {
                        coreInventory.setInventorySlotContents(3, itemResult);
                        a.setCurrentWork(0);
                    } else if (coreInventory.getStackInSlot(3).getItem().equals(itemResult.getItem()) &&
                            coreInventory.getStackInSlot(3).stackSize + itemResult.stackSize <= itemResult.getItem().getItemStackLimit(itemResult)) {
                        coreInventory.getStackInSlot(3).stackSize += itemResult.stackSize;
                        a.setCurrentWork(0);
                    }
                }
                if (fluidResult != null) {
                    if (getOutputTank().fill(fluidResult, false) == fluidResult.amount) {
                        getOutputTank().fill(fluidResult, true);
                        a.setCurrentWork(0);
                    }
                }
            });
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

    private void assembleRecursive(Set<SyncableCoord> controllers, Set<SyncableCoord> launchPads, SyncableCoord io, HashSet<ChunkPosition> set, int x, int y, int z) {
        ChunkPosition pos = new ChunkPosition(x, y, z);
        if (set.contains(pos)) return;

        set.add(pos);
        Block block = worldObj.getBlock(x, y, z);
        if (!(block instanceof BlockLaunchControllerExtension || block instanceof BlockLaunchPadController || block instanceof BlockLaunchPad || block instanceof BlockLaunchPadIO)) return;

        SyncableCoord coord = new SyncableCoord(worldObj, x, y, z);
        if(block instanceof BlockLaunchPad)
            launchPads.add(coord);
        else if(block instanceof BlockLaunchControllerExtension)
            controllers.add(coord);
        else if(block instanceof BlockLaunchPadIO) {
            io.assign(coord);
        }

        TileEntity tileEntity = worldObj.getTileEntity(x, y, z);
        if(tileEntity != null && tileEntity instanceof TileEntityMultiblockPart) {
            ((TileEntityMultiblockPart) tileEntity).setBase(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));
        }

        assembleRecursive(controllers, launchPads, io, set, x - 1, y, z);
        assembleRecursive(controllers, launchPads, io, set, x, y - 1, z);
        assembleRecursive(controllers, launchPads, io, set, x, y, z - 1);
        assembleRecursive(controllers, launchPads, io, set, x + 1, y, z);
        assembleRecursive(controllers, launchPads, io, set, x, y + 1, z);
        assembleRecursive(controllers, launchPads, io, set, x, y, z + 1);
    }

    @Override
    public IInventory getInventory() {
        return coreInventory;
    }

    public IInventory getGhostInventory() {
        return inventory;
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
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
    }

    @Override
    public void onSync(Set<ISyncableObject> changes) {
    }

    @Override
    public void neighborPlaced(Block block) {
        rebuild();
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        return energyStorage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return true;
    }

    public SyncableEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public IInventory getCoreInventory() {
        return coreInventory;
    }

    public MonitoredTank getTank(int id) {
        switch (id) {
            case 0: return getOutputTank();
            case 1: return ghostTank1;
            case 2: return ghostTank2;
        }
        return getOutputTank();
    }

    public boolean extractEnergy(int amount) {
        boolean result = energyStorage.extractEnergy(amount, true) == amount;
        if(result) energyStorage.extractEnergy(amount, false);
        return result;
    }

    public MonitoredTank getOutputTank() {
        TileEntity tileEntity = worldObj.getTileEntity(io.getX(), io.getY(), io.getZ());
        if(tileEntity != null && tileEntity instanceof TileEntityLaunchPadIO)
            return ((TileEntityLaunchPadIO) tileEntity).getOutputTank();
        return null;
    }

    public IReadableBitMap<ForgeDirection> getReadableBatteryInput() {
        return batteryInputs;
    }

    public IWriteableBitMap<ForgeDirection> getWritableBatteryInput() {
        return BitMapUtils.createRpcAdapter(createRpcProxy(batteryInputs, IRpcDirectionBitMap.class));
    }
}
