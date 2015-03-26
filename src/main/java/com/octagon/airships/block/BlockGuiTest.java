package com.octagon.airships.block;

import com.octagon.airships.MassEffectShips;
import net.minecraft.block.material.Material;
import openmods.block.OpenBlock;

public class BlockGuiTest extends OpenBlock {
    public BlockGuiTest() {
        super(Material.rock);
    }

    @Override
    protected Object getModInstance() {
        return MassEffectShips.instance;
    }
}
