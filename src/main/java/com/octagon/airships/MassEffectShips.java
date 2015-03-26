package com.octagon.airships;

import com.octagon.airships.handler.GuiHandler;
import com.octagon.airships.init.ModBlocks;
import com.octagon.airships.init.ModFluids;
import com.octagon.airships.init.ModItems;
import com.octagon.airships.init.ModRecipes;
import com.octagon.airships.item.ItemProbe;
import com.octagon.airships.proxy.IProxy;
import com.octagon.airships.reference.Config;
import com.octagon.airships.reference.Reference;
import com.octagon.airships.sync.rpc.IRadiusChanger;
import com.octagon.airships.util.LogHelper;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import openmods.config.game.FactoryRegistry;
import openmods.config.game.ModStartupHelper;
import openmods.config.properties.ConfigProcessing;
import openmods.network.rpc.RpcCallDispatcher;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY_CLASS)
public class MassEffectShips {

    @Mod.Instance(Reference.MOD_ID)
    public static MassEffectShips instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;
    public static int renderIdFlat;
    public static int renderIdFull;

    private final ModStartupHelper startupHelper = new ModStartupHelper("masseffectships") {

        @Override
        protected void populateConfig(Configuration config) {
            ConfigProcessing.processAnnotations("MassEffectShips", config, Config.EnergyStorage.class);
            ConfigProcessing.processAnnotations("MassEffectShips", config, Config.EnergyUsage.class);
            ConfigProcessing.processAnnotations("MassEffectShips", config, Config.Sizes.class);
        }

        @Override
        protected void setupItemFactory(FactoryRegistry<Item> itemFactory) {
            itemFactory.registerFactory("probe", () -> new ItemProbe(ItemProbe.Type.EMPTY));
            itemFactory.registerFactory("probeMining", () -> new ItemProbe(ItemProbe.Type.MINING));
            itemFactory.registerFactory("probeGas", () -> new ItemProbe(ItemProbe.Type.GAS));
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        startupHelper.registerBlocksHolder(ModBlocks.class);
        startupHelper.registerItemsHolder(ModItems.class);
        ModFluids.init();

        startupHelper.preInit(event.getSuggestedConfigurationFile());

        RpcCallDispatcher.INSTANCE.startRegistration()
                .registerInterface(IRadiusChanger.class);

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        ModRecipes.init();

        proxy.preInit();

        LogHelper.info("Pre Initialization complete.");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        proxy.init();
        proxy.registerRenderInformation();

        LogHelper.info("Initialization complete.");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();

        LogHelper.info("Post Initialization complete.");
    }

    public static String getModId() {
        return Reference.MOD_ID;
    }
}
