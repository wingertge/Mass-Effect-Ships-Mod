package com.octagon.airships.block.tileentity;

import net.minecraft.block.Block;

public interface INeighborPlacedAware {
    void neighborPlaced(Block block);
}
