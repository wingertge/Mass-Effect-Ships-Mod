package com.octagon.airships.block.tileentity;

import com.octagon.airships.block.multiblock.BlockMultiblockBase;
import com.octagon.airships.block.multiblock.MultiblockStructure;
import com.octagon.airships.block.multiblock.TileEntityMultiblock;
import com.octagon.airships.sync.SyncableCoord;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import openmods.api.IActivateAwareTile;
import openmods.api.IBreakAwareTile;
import openmods.api.INeighbourAwareTile;
import openmods.sync.ISyncListener;
import openmods.sync.ISyncableObject;
import openmods.tileentity.SyncedTileEntity;

import java.util.Set;

public class TileEntityMultiblockPart extends SyncedTileEntity implements ISyncListener, IBreakAwareTile, IActivateAwareTile {

    private MultiblockStructure structure;
    private SyncableCoord base;
    private boolean propagated;

    public TileEntityMultiblockPart() {
        syncMap.addSyncListener(this);
    }

    @Override
    protected void createSyncedFields() {
        base = new SyncableCoord();
        structure = new MultiblockStructure(base);
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
    public void onSync(Set<ISyncableObject> changes) {

    }

    @Override
    public void onBlockBroken() {
        if(getStructure() != null)
            getStructure().breakBlock(worldObj);
    }


    public boolean onNeighbourPlaced(Block block) {
        TileEntity tileEntity = worldObj.getTileEntity(base.getX(), base.getY(), base.getZ());
        if(tileEntity != null && tileEntity instanceof TileEntityMultiblock && tileEntity instanceof INeighbourAwareTile) {
            ((INeighbourAwareTile) tileEntity).onNeighbourChanged(block);
            return true;
        }
        return false;
    }


    @Override
    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        Block block = worldObj.getBlock(base.getX(), base.getY(), base.getZ());
        return block != null && block instanceof BlockMultiblockBase && block.onBlockActivated(worldObj, base.getX(), base.getY(), base.getZ(), player, side, hitX, hitY, hitZ);
    }
}
