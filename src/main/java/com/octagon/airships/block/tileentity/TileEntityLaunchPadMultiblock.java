package com.octagon.airships.block.tileentity;

import com.google.common.collect.Sets;
import com.octagon.airships.block.BlockLaunchControllerExtension;
import com.octagon.airships.block.BlockLaunchPad;
import com.octagon.airships.block.BlockLaunchPadController;
import com.octagon.airships.block.multiblock.MultiblockStructure;
import com.octagon.airships.block.multiblock.TileEntityMultiblock;
import com.octagon.airships.sync.SyncableCoord;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import openmods.utils.BlockNotifyFlags;

import java.util.HashSet;
import java.util.Set;

public class TileEntityLaunchPadMultiblock extends TileEntityMultiblock {
    private MultiblockStructure structure;
    private int rebuildTimer;
    private Set<SyncableCoord> controllers;
    private Set<SyncableCoord> launchPads;

    public TileEntityLaunchPadMultiblock() {
        rebuildTimer = 2;
    }

    @Override
    protected void createSyncedFields() {
        structure = new MultiblockStructure(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));
        controllers = Sets.newHashSet();
        launchPads = Sets.newHashSet();
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
        if(!worldObj.isRemote) return;

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
        });

        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, BlockNotifyFlags.ALL);

        if(!worldObj.isRemote) sync();
    }

    @Override
    public void queueStructureValidation() {
        rebuildTimer = 1;
    }

    @Override
    public void updateEntity() {
        if(rebuildTimer != 0) {
            rebuildTimer--;
            if(rebuildTimer == 0) rebuild();
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
}
