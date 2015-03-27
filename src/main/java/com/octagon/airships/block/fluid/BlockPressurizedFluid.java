package com.octagon.airships.block.fluid;

import com.octagon.airships.block.BlockAirshipsFluidClassic;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public abstract class BlockPressurizedFluid extends BlockAirshipsFluidClassic {
    protected BlockPressurizedFluid(Fluid fluid, Material material) {
        super(fluid, material);
    }
}
