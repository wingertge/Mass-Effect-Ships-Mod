package com.octagon.airships.block.multiblock;

import com.octagon.airships.MassEffectShips;
import net.minecraft.block.material.Material;
import openmods.block.OpenBlock;

public abstract class BlockMultiblockBase extends OpenBlock {
    protected BlockMultiblockBase(Material material) {
        super(material);
    }

    public BlockMultiblockBase() {
        super(Material.iron);
    }

    @Override
    protected Object getModInstance() {
        return MassEffectShips.instance;
    }
}
