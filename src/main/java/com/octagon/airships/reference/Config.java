package com.octagon.airships.reference;

import openmods.config.properties.ConfigProperty;

public class Config {
    public static final class EnergyUsage {
        public static final String CATEGORY = "energy-usage";

        @ConfigProperty(category = CATEGORY, name = "alloyMixerEnergyUsage", comment = "Energy usage for the alloy mixer")
        public static int ALLOY_MIXER = 20;

        @ConfigProperty(category = CATEGORY, name = "ceramicFormerEnergyUsage", comment = "Energy usage for the ceramic former")
        public static int CERAMIC_FORMER = 10;
    }

    public static final class EnergyStorage {
        public static final String CATEGORY = "energy-storage";

        @ConfigProperty(category = CATEGORY, name = "genericMachineEnergyStorage", comment = "Energy storage for a generic machine")
        public static int MACHINE_GENERIC = 100000;

        @ConfigProperty(category = CATEGORY, name = "alloyMixerEnergyStorage", comment = "Energy storage for the alloy mixer")
        public static int ALLOY_MIXER = 100000;

        @ConfigProperty(category = CATEGORY, name = "ceramicFormerEnergyStorage", comment = "Energy storage for the ceramic former")
        public static int CERAMIC_FORMER = 100000;
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
}
