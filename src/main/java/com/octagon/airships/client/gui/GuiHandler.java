package com.octagon.airships.client.gui;

import com.octagon.airships.reference.GUIs;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import openmods.api.IHasGui;
import openmods.block.OpenBlock;
import openmods.gui.CommonGuiHandler;

public class GuiHandler extends CommonGuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if(id == GUIs.DYNAMIC.ordinal() || id == OpenBlock.OPEN_MODS_TE_GUI) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof IHasGui) return ((IHasGui)tile).getServerGui(player);
            return null;
        } else {
            return super.getServerGuiElement(id, player, world, x, y, z);
        }
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if(id == GUIs.DYNAMIC.ordinal() || id == OpenBlock.OPEN_MODS_TE_GUI) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof IHasGui) return ((IHasGui)tile).getClientGui(player);
            return null;
        } else {
            return super.getClientGuiElement(id, player, world, x, y, z);
        }
    }
}
