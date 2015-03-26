package openmods.gui;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import openmods.api.IHasGui;
import openmods.block.OpenBlock;
import cpw.mods.fml.common.network.IGuiHandler;

public class ClientGuiHandler extends CommonGuiHandler {

	public ClientGuiHandler() {}

	public ClientGuiHandler(IGuiHandler wrappedHandler) {
		super(wrappedHandler);
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (world instanceof WorldClient) {
			if (id != OpenBlock.OPEN_MODS_TE_GUI) return wrappedHandler != null? wrappedHandler.getClientGuiElement(id, player, world, x, y, z) : null;
			else {
				TileEntity tile = world.getTileEntity(x, y, z);
				if (tile instanceof IHasGui) return ((IHasGui)tile).getClientGui(player);
			}
		}
		return null;
	}
}
