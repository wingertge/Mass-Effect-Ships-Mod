package com.octagon.airships.init;

import com.octagon.airships.fluid.AirshipsFluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {
    public static AirshipsFluid liquidDeuterium;
    public static AirshipsFluid liquidHelium3;
    public static AirshipsFluid antiprotons;
    public static AirshipsFluid liquidHydrogen;
    public static AirshipsFluid liquidOxygen;

    public static void init() {
        liquidDeuterium = new AirshipsFluid("masseffectships_deuteriumLiquid");
        liquidDeuterium.setBlock(ModBlocks.deuteriumLiquid);

        FluidRegistry.registerFluid(liquidDeuterium);

        liquidHelium3 = new AirshipsFluid("masseffectships_helium3Liquid");
        liquidHelium3.setBlock(ModBlocks.helium3Liquid);

        FluidRegistry.registerFluid(liquidHelium3);

        antiprotons = new AirshipsFluid("masseffectships_antiprotons");
        antiprotons.setBlock(ModBlocks.antiprotons);

        FluidRegistry.registerFluid(antiprotons);

        liquidHydrogen = new AirshipsFluid("masseffectships_hydrogenLiquid");
        liquidHydrogen.setBlock(ModBlocks.hydrogenLiquid);

        FluidRegistry.registerFluid(liquidHydrogen);

        liquidOxygen = new AirshipsFluid("masseffectships_oxygenLiquid");
        liquidOxygen.setBlock(ModBlocks.oxygenLiquid);

        FluidRegistry.registerFluid(liquidOxygen);
    }
}
