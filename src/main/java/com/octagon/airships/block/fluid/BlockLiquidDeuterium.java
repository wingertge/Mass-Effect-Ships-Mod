package com.octagon.airships.block.fluid;

import com.octagon.airships.init.ModFluids;
import net.minecraft.block.material.Material;

public class BlockLiquidDeuterium extends BlockPressurizedFluid {
    public BlockLiquidDeuterium() {
        super(ModFluids.liquidDeuterium, Material.water);
    }
}
