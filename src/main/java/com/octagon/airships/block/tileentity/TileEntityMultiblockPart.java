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
import openmods.sync.ISyncListener;
import openmods.sync.ISyncableObject;
import openmods.sync.drops.DroppableTileEntity;

import java.util.Set;

public class TileEntityMultiblockPart extends DroppableTileEntity implements ISyncListener, IBreakAwareTile, IActivateAwareTile {

    private MultiblockStructure structure;
    private SyncableCoord base;

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
    public void onSync(Set<ISyncableObject> changes) {

    }

    @Override
    public void onBlockBroken() {
        if(getStructure() != null)
            getStructure().breakBlock(getWorldObj());
    }


    public boolean onNeighbourPlaced(Block block) {
        TileEntity tileEntity = getWorldObj().getTileEntity(getBase().getX(), getBase().getY(), getBase().getZ());
        if(tileEntity != null && tileEntity instanceof TileEntityMultiblock && tileEntity instanceof INeighborPlacedAware) {
            ((INeighborPlacedAware) tileEntity).neighborPlaced(block);
            return true;
        }
        return false;
    }


    @Override
    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        Block block = getWorldObj().getBlock(getBase().getX(), getBase().getY(), getBase().getZ());
        return block != null && block instanceof BlockMultiblockBase && block.onBlockActivated(getWorldObj(), getBase().getX(), getBase().getY(), getBase().getZ(), player, side, hitX, hitY, hitZ);
    }
}
