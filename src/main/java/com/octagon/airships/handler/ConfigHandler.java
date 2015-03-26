package com.octagon.airships.handler;

import com.octagon.airships.reference.Config;
import com.octagon.airships.reference.Reference;
import com.octagon.airships.util.LangUtil;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {
    public static Configuration config;

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(event.modID.equalsIgnoreCase(Reference.MOD_ID))
            loadConfig();
    }

    private static void loadConfig() {
        Config.Sizes.MAX_CORE_RADIUS = config.getInt("maxCoreRadius", Config.Sizes.CATEGORY, Config.Sizes.MAX_CORE_RADIUS, 1, Integer.MAX_VALUE, "Maximum radius for the eezo core");
        Config.Sizes.MAX_SHIP_SIZE = config.getInt("maxShipSize", Config.Sizes.CATEGORY, Config.Sizes.MAX_SHIP_SIZE, 1, Integer.MAX_VALUE, "Maximum amount of blocks in ship");
        Config.EnergyStorage.MACHINE_GENERIC = config.getInt("machineEnergyStorage", Config.EnergyStorage.CATEGORY, Config.EnergyStorage.MACHINE_GENERIC, 1, Integer.MAX_VALUE, "Maximum amount of energy stored in a generic machine");
        Config.EnergyStorage.ALLOY_MIXER = config.getInt("alloyMixerEnergyStorage", Config.EnergyStorage.CATEGORY, Config.EnergyStorage.ALLOY_MIXER, 1, Integer.MAX_VALUE, "Maximum amount of energy stored in an alloy mixer");
        Config.EnergyStorage.CERAMIC_FORMER = config.getInt("ceramicFormerEnergyStorage", Config.EnergyStorage.CATEGORY, Config.EnergyStorage.CERAMIC_FORMER, 1, Integer.MAX_VALUE, "Maximum amount of energy stored in a ceramic former");
        Config.EnergyUsage.ALLOY_MIXER = config.getInt("alloyMixerUsage", Config.EnergyUsage.CATEGORY, Config.EnergyUsage.ALLOY_MIXER, 1, Integer.MAX_VALUE, LangUtil.localize("alloyMixerUsageComment", true));
        Config.EnergyUsage.CERAMIC_FORMER = config.getInt("ceramicFormerUsage", Config.EnergyUsage.CATEGORY, Config.EnergyUsage.CERAMIC_FORMER, 1, Integer.MAX_VALUE, LangUtil.localize("ceramicFormerUsageComment", true));

        if (config.hasChanged())
            config.save();
    }
}
