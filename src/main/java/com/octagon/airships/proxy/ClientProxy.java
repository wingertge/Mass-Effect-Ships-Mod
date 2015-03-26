package com.octagon.airships.proxy;

import com.octagon.airships.MassEffectShips;
import com.octagon.airships.block.tileentity.TileEntityEezoCoreMultiblock;
import com.octagon.airships.client.render.tileentity.TileEntityEezoCoreBaseRenderer;
import com.octagon.airships.init.ModBlocks;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ICrashCallable;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.DimensionManager;
import openmods.Log;
import openmods.OpenMods;
import openmods.config.properties.CommandConfig;
import openmods.gui.ClientGuiHandler;
import openmods.movement.PlayerMovementManager;
import openmods.renderer.BlockRenderingHandler;
import openmods.renderer.BlockRenderingValidator;
import openmods.source.CommandSource;
import openmods.stencil.FramebufferConstants;
import openmods.stencil.StencilPoolManager;
import openmods.utils.render.RenderUtils;

import java.io.File;

public class ClientProxy implements IProxy {

    @Override
    public void registerRenderInformation() {
        {
            MassEffectShips.renderIdFull = RenderingRegistry.getNextAvailableRenderId();
            final BlockRenderingHandler blockRenderingHandler = new BlockRenderingHandler(MassEffectShips.renderIdFull, true);

            RenderingRegistry.registerBlockHandler(blockRenderingHandler);
        }

        {
            MassEffectShips.renderIdFlat = RenderingRegistry.getNextAvailableRenderId();
            final BlockRenderingHandler blockRenderingHandler = new BlockRenderingHandler(MassEffectShips.renderIdFlat, false);

            RenderingRegistry.registerBlockHandler(blockRenderingHandler);
        }

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEezoCoreMultiblock.class, new TileEntityEezoCoreBaseRenderer());

        new BlockRenderingValidator().verifyBlocks(ModBlocks.class);
    }

    @Override
    public EntityPlayer getThePlayer() {
        return FMLClientHandler.instance().getClient().thePlayer;
    }

    @Override
    public boolean isClientPlayer(Entity player) {
        return player instanceof EntityPlayerSP;
    }

    @Override
    public long getTicks(World worldObj) {
        if (worldObj != null) { return worldObj.getTotalWorldTime(); }
        World cWorld = getClientWorld();
        if (cWorld != null) return cWorld.getTotalWorldTime();
        return 0;
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().theWorld;
    }

    @Override
    public World getServerWorld(int id) {
        return DimensionManager.getWorld(id);
    }

    @Override
    public File getMinecraftDir() {
        return Minecraft.getMinecraft().mcDataDir;
    }

    @Override
    public String getLogFileName() {
        return "ForgeModLoader-client-0.log";
    }

    @Override
    public IGuiHandler wrapHandler(IGuiHandler modSpecificHandler) {
        return new ClientGuiHandler(modSpecificHandler);
    }

    @Override
    public void preInit() {
        ClientCommandHandler.instance.registerCommand(new CommandConfig("om_config_c", false));
        ClientCommandHandler.instance.registerCommand(new CommandSource("om_source_c", false, OpenMods.instance.getCollector()));

        RenderUtils.registerFogUpdater();

        FMLCommonHandler.instance().registerCrashCallable(new ICrashCallable() {
            @Override
            public String call() throws Exception {
                final StencilPoolManager.StencilPoolImpl pool = StencilPoolManager.pool();
                return String.format("Function set: %s, pool: %s, bits: %s", FramebufferConstants.getMethodSet(), pool.getType(), pool.getSize());
            }

            @Override
            public String getLabel() {
                return "Stencil buffer state";
            }
        });
    }

    @Override
    public void init() {}

    @Override
    public void postInit() {
        if (!PlayerMovementManager.isCallbackInjected()) {
            Log.info("EntityPlayerSP movement callback patch not applied, using legacy solution");
            FMLCommonHandler.instance().bus().register(new PlayerMovementManager.LegacyTickHandler());
        }
    }

    @Override
    public void setNowPlayingTitle(String nowPlaying) {
        Minecraft.getMinecraft().ingameGUI.setRecordPlayingMessage(nowPlaying);
    }

    @Override
    public EntityPlayer getPlayerFromHandler(INetHandler handler) {
        if (handler instanceof NetHandlerPlayServer) return ((NetHandlerPlayServer)handler).playerEntity;

        if (handler instanceof NetHandlerPlayClient) return getThePlayer();

        return null;
    }
}
