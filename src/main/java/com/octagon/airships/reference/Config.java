package com.octagon.airships.reference;

import openmods.config.properties.ConfigProperty;

public class Config {
    public static final class EnergyUsage {
        public static final String CATEGORY = "energy-usage";

        @ConfigProperty(category = CATEGORY, name = "alloyMixerEnergyUsage", comment = "Energy usage for the alloy mixer")
        public static int ALLOY_MIXER = 20;

        @ConfigProperty(category = CATEGORY, name = "ceramicFormerEnergyUsage", comment = "Energy usage for the ceramic former")
        public static int CERAMIC_FORMER = 10;

        @ConfigProperty(category = CATEGORY, name = "launchPadEnergyUsage", comment = "Energy usage per active launch pad")
        public static int LAUNCH_PAD = 10;

        @ConfigProperty(category = CATEGORY, name = "deuteriumExtractorEnergyUsage", comment = "Energy usage for the deuterium extractor.")
        public static int DEUTERIUM_EXTRACTOR = 5;
    }

    public static final class EnergyStorage {
        public static final String CATEGORY = "energy-storage";

        @ConfigProperty(category = CATEGORY, name = "genericMachineEnergyStorage", comment = "Energy storage for a generic machine")
        public static int MACHINE_GENERIC = 100000;

        @ConfigProperty(category = CATEGORY, name = "alloyMixerEnergyStorage", comment = "Energy storage for the alloy mixer")
        public static int ALLOY_MIXER = 100000;

        @ConfigProperty(category = CATEGORY, name = "ceramicFormerEnergyStorage", comment = "Energy storage for the ceramic former")
        public static int CERAMIC_FORMER = 100000;

        @ConfigProperty(category = CATEGORY, name = "launchPadControllerEnergyStorage", comment = "Energy storage per launch pad multiblock.")
        public static int LAUNCH_PAD_CONTROLLER = 1000000;

        @ConfigProperty(category = CATEGORY, name = "electrolyzerEnergyStorage", comment = "Energy storage of the electrolyzer")
        public static int ELECTROLYZER = 100000;
    }

    public static final class Sizes {
        public static final String CATEGORY = "sizes";

        @ConfigProperty(category = CATEGORY, name = "maxShipSize", comment = "Max Blocks in a single ship")
        public static int MAX_SHIP_SIZE = 256000;

        @ConfigProperty(category = CATEGORY, name = "maxCoreRadius", comment = "Max eezo core radius")
        public static int MAX_CORE_RADIUS = 20;

        @ConfigProperty(category = CATEGORY, name = "guideRenderDistanceSq", comment = "Squared render distance for eezo core guide")
        public static double EEZO_CORE_RENDER_DISTANCE_SQ = 2500;
    }

    public static final class FluidStorage {
        public static final String CATEGORY = "fluid-storage";

        @ConfigProperty(category = CATEGORY, name = "launchPadOutputFluidStorage", comment = "Internal tank capacity of launch pad output (in mB)")
        public static int LAUNCH_PAD_OUTPUT = 16000;

        @ConfigProperty(category = CATEGORY, name = "launchPadFluidStorage", comment = "Max fluid storage per launch pad (in mB)")
        public static int LAUNCH_PAD = 4000;

        @ConfigProperty(category = CATEGORY, name = "deuteriumExtractorInputFluidStorage")
        public static int DEUTERIUM_EXTRACTOR_IN = 32000;

        @ConfigProperty(category = CATEGORY, name = "deuteriumExtractorOutputFluidStorage")
        public static int DEUTERIUM_EXTRACTOR_OUT = 1000;

        @ConfigProperty(category = CATEGORY, name = "electrolyzerInputFluidStorage")
        public static int ELECTROLYZER_IN = 16000;

        @ConfigProperty(category = CATEGORY, name = "electrolyzerOutputFluidStorage")
        public static int ELECTROLYZER_OUT = 8000;
    }

    public static final class Work {
        public static final String CATEGORY = "fluid-storage";

        @ConfigProperty(category = CATEGORY, name = "deuteriumWorkCost")
        public static int DEUTERIUM = 10;
    }
}
