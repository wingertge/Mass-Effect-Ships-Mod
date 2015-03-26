package com.octagon.airships.block;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;

public abstract class BlockAirshipsFluid extends BlockFluidBase {
    protected BlockAirshipsFluid(Fluid fluid, Material material) {
        super(fluid, material);
    }
}
