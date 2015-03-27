package com.octagon.airships.block.fluid;

import com.octagon.airships.init.ModFluids;
import net.minecraft.block.material.Material;

public class BlockLiquidHydrogen extends BlockPressurizedFluid {
    public BlockLiquidHydrogen() {
        super(ModFluids.liquidHydrogen, Material.water);
    }
}
