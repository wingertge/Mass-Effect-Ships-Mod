package com.octagon.airships.block.fluid;

import com.octagon.airships.init.ModFluids;
import net.minecraft.block.material.Material;

public class BlockLiquidOxygen extends BlockPressurizedFluid {
    public BlockLiquidOxygen() {
        super(ModFluids.liquidOxygen, Material.water);
    }
}
