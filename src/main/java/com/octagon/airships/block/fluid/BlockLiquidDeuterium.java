package com.octagon.airships.block.fluid;

import com.octagon.airships.block.BlockAirshipsFluidClassic;
import com.octagon.airships.init.ModFluids;
import net.minecraft.block.material.Material;

public class BlockLiquidDeuterium extends BlockAirshipsFluidClassic {
    public BlockLiquidDeuterium() {
        super(ModFluids.liquidDeuterium, Material.water);
    }
}
