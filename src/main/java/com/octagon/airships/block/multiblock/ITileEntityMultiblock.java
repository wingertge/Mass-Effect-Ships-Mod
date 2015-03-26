package com.octagon.airships.block.multiblock;

import com.octagon.airships.sync.SyncableCoord;

public interface ITileEntityMultiblock {
    public MultiblockStructure getStructure();
    public void setStructure(MultiblockStructure structure);
    public SyncableCoord getBaseBlock();
    public boolean isComplete();
    public void rebuild();
}
