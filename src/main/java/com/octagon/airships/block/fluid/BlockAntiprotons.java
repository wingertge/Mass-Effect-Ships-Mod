package com.octagon.airships.block.fluid;

import com.octagon.airships.init.ModFluids;
import net.minecraft.block.material.Material;

public class BlockAntiprotons extends BlockPressurizedFluid {
    public BlockAntiprotons() {
        super(ModFluids.antiprotons, Material.water);
    }
}
