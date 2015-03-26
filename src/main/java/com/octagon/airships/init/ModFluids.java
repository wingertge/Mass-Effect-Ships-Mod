package com.octagon.airships.init;

import com.octagon.airships.fluid.AirshipsFluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {
    public static AirshipsFluid liquidDeuterium;

    public static void init() {
        liquidDeuterium = new AirshipsFluid("masseffectships_deuteriumLiquid");
        liquidDeuterium.setBlock(ModBlocks.deuteriumLiquid);

        FluidRegistry.registerFluid(liquidDeuterium);
    }
}
