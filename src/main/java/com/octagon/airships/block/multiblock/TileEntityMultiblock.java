package com.octagon.airships.block.multiblock;

import openmods.sync.drops.DroppableTileEntity;

public abstract class TileEntityMultiblock extends DroppableTileEntity implements ITileEntityMultiblock {
    protected MultiblockStructure structure;

    protected TileEntityMultiblock() {

    }

    @Override
    public void setStructure(MultiblockStructure structure) { this.structure = structure; }

    @Override
    public MultiblockStructure getStructure() {
        return structure;
    }

    public abstract void queueStructureValidation();
}
