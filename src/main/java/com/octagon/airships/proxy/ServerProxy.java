package com.octagon.airships.proxy;

import com.octagon.airships.handler.NetworkHandler;
import com.octagon.airships.network.MessageTileEntityUpdate;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import openmods.gui.CommonGuiHandler;

import java.io.File;

public class ServerProxy implements IProxy {

    @Override
    public EntityPlayer getThePlayer() {
        return null;
    }

    @Override
    public boolean isClientPlayer(Entity player) {
        return false;
    }

    @Override
    public long getTicks(World worldObj) {
        return worldObj.getTotalWorldTime();
    }

    @Override
    public World getClientWorld() {
        return null;
    }

    @Override
    public World getServerWorld(int id) {
        return DimensionManager.getWorld(id);
    }

    @Override
    public File getMinecraftDir() {
        return MinecraftServer.getServer().getFile("");
    }

    @Override
    public String getLogFileName() {
        return "ForgeModLoader-server-0.log";
    }

    @Override
    public IGuiHandler wrapHandler(IGuiHandler modSpecificHandler) {
        return new CommonGuiHandler(modSpecificHandler);
    }

    @Override
    public void preInit() {}

    @Override
    public void init() {}

    @Override
    public void postInit() {}

    @Override
    public void setNowPlayingTitle(String nowPlaying) {}

    @Override
    public EntityPlayer getPlayerFromHandler(INetHandler handler) {
        if (handler instanceof NetHandlerPlayServer) return ((NetHandlerPlayServer)handler).playerEntity;

        return null;
    }

    @Override
    public void registerRenderInformation() {

    }
}
