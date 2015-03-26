package openmods.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovementInput;
import net.minecraftforge.common.MinecraftForge;
import openmods.movement.PlayerMovementEvent.Type;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

public class PlayerMovementManager {

	static boolean callbackInjected = false;

	private static boolean wasJumping = false;
	private static boolean wasSneaking = false;

	private PlayerMovementManager() {}

	public static void updateMovementState(MovementInput input, EntityPlayer owner) {
		if (input.jump && !wasJumping) input.jump = postMovementEvent(owner, PlayerMovementEvent.Type.JUMP);
		if (input.sneak && !wasSneaking) input.sneak = postMovementEvent(owner, PlayerMovementEvent.Type.SNEAK);

		wasJumping = input.jump;
		wasSneaking = input.sneak;
	}

	private static boolean postMovementEvent(EntityPlayer player, Type type) {
		return !MinecraftForge.EVENT_BUS.post(new PlayerMovementEvent(player, type));
	}

	public static boolean isCallbackInjected() {
		return callbackInjected;
	}

	public static class LegacyTickHandler {
		@SubscribeEvent
		public void onClientTick(ClientTickEvent evt) {
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			if (player != null) PlayerMovementManager.updateMovementState(player.movementInput, player);
		}
	}
}
