package openmods.utils.render;

import java.util.EnumSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.block.BlockRotationMode;
import openmods.block.OpenBlock;
import openmods.utils.ColorUtils.RGB;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderUtils {

	public static class FogColorUpdater {
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public void onFogColor(EntityViewRenderEvent.FogColors evt) {
			fogRed = evt.red;
			fogGreen = evt.green;
			fogBlue = evt.blue;
		}
	}

	private static float fogRed;
	private static float fogGreen;
	private static float fogBlue;

	public static void setupBillboard(Entity rve) {
		GL11.glRotatef(-rve.rotationYaw, 0, 1, 0);
		GL11.glRotatef(rve.rotationPitch, 1, 0, 0);
	}

	/**
	 * Please! For the love of sanity. Do not store this instance ANYWHERE!
	 * If you set it to a TE or Entity, Please remove it after you're done!
	 * THANK YOU!
	 *
	 * @return Returns a world for rendering with
	 */
	public static World getRenderWorld() {
		if (Minecraft.getMinecraft() != null) return Minecraft.getMinecraft().theWorld;
		return null;
	}

	public static double interpolateValue(double current, double prev, float partialTickTime) {
		return prev + partialTickTime * (current - prev);
	}

	public static float interpolateYaw(Entity e, float f) {
		return e.prevRotationYaw + (e.rotationYaw - e.prevRotationYaw) * f;
	}

	public static float interpolatePitch(Entity e, float f) {
		return e.prevRotationPitch + (e.rotationPitch - e.prevRotationPitch) * f;
	}

	public static void translateToPlayer(Entity e, float partialTickTime) {
		GL11.glTranslated(
				interpolateValue(e.posX, e.prevPosX, partialTickTime) - RenderManager.renderPosX,
				interpolateValue(e.posY, e.prevPosY, partialTickTime) - RenderManager.renderPosY,
				interpolateValue(e.posZ, e.prevPosZ, partialTickTime) - RenderManager.renderPosZ);
	}

	public static void translateToWorld(Entity e, float partialTickTime) {
		GL11.glTranslated(
				interpolateValue(e.posX, e.prevPosX, partialTickTime),
				interpolateValue(e.posY, e.prevPosY, partialTickTime),
				interpolateValue(e.posZ, e.prevPosZ, partialTickTime));
	}

	public static void rotateFacesOnRenderer(OpenBlock block, ForgeDirection rotation, RenderBlocks renderer) {
		BlockRotationMode mode = block.getRotationMode();
		switch (mode) {
			case SIX_DIRECTIONS:
				switch (rotation) {
					case DOWN:
						renderer.uvRotateSouth = 3;
						renderer.uvRotateNorth = 3;
						renderer.uvRotateEast = 3;
						renderer.uvRotateWest = 3;
						break;
					case EAST:
						renderer.uvRotateTop = 1;
						renderer.uvRotateBottom = 2;
						renderer.uvRotateWest = 1;
						renderer.uvRotateEast = 2;
						break;
					case NORTH:
						renderer.uvRotateNorth = 2;
						renderer.uvRotateSouth = 1;
						break;
					case SOUTH:
						renderer.uvRotateTop = 3;
						renderer.uvRotateBottom = 3;
						renderer.uvRotateNorth = 1;
						renderer.uvRotateSouth = 2;
						break;
					case UNKNOWN:
						break;
					case UP:
						break;
					case WEST:
						renderer.uvRotateTop = 2;
						renderer.uvRotateBottom = 1;
						renderer.uvRotateWest = 2;
						renderer.uvRotateEast = 1;
						break;
					default:
						break;

				}
				break;
			case FOUR_DIRECTIONS:
				switch (rotation) {
					case EAST:
						renderer.uvRotateTop = 1;
						break;
					case WEST:
						renderer.uvRotateTop = 2;
						break;
					case SOUTH:
						renderer.uvRotateTop = 3;
						break;
					default:
						break;
				}
				break;
			default:
				break;

		}

	}

	public static void resetFacesOnRenderer(RenderBlocks renderer) {
		renderer.uvRotateBottom = 0;
		renderer.uvRotateEast = 0;
		renderer.uvRotateNorth = 0;
		renderer.uvRotateSouth = 0;
		renderer.uvRotateTop = 0;
		renderer.uvRotateWest = 0;
		renderer.flipTexture = false;
	}

	public static void renderInventoryBlock(RenderBlocks renderer, Block block, ForgeDirection rotation) {
		renderInventoryBlock(renderer, block, rotation, -1);
	}

	public static void renderInventoryBlock(RenderBlocks renderer, Block block, ForgeDirection rotation, int colorMultiplier) {
		renderInventoryBlock(renderer, block, rotation, colorMultiplier, EnumSet.allOf(ForgeDirection.class));
	}

	public static void renderInventoryBlock(RenderBlocks renderer, Block block, ForgeDirection rotation, int colorMultiplier, Set<ForgeDirection> enabledSides) {
		Tessellator tessellator = Tessellator.instance;
		block.setBlockBoundsForItemRender();
		renderer.setRenderBoundsFromBlock(block);

		float r;
		float g;
		float b;
		if (colorMultiplier > -1)
		{
			r = (colorMultiplier >> 16 & 255) / 255.0F;
			g = (colorMultiplier >> 8 & 255) / 255.0F;
			b = (colorMultiplier & 255) / 255.0F;
			GL11.glColor4f(r, g, b, 1.0F);
		}
		// Learn to matrix, please push and pop :D -- NC
		GL11.glPushMatrix();
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		int metadata = rotation.ordinal();
		if (enabledSides.contains(ForgeDirection.DOWN)) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
			tessellator.draw();
		}
		if (enabledSides.contains(ForgeDirection.UP)) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
			tessellator.draw();
		}
		if (enabledSides.contains(ForgeDirection.SOUTH)) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
			tessellator.draw();
		}
		if (enabledSides.contains(ForgeDirection.NORTH)) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
			tessellator.draw();
		}
		if (enabledSides.contains(ForgeDirection.WEST)) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
			tessellator.draw();
		}
		if (enabledSides.contains(ForgeDirection.EAST)) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
			tessellator.draw();
		}
		GL11.glPopMatrix();
	}

	public static void renderCube(Tessellator tes, double x1, double y1, double z1, double x2, double y2, double z2) {
		tes.addVertex(x1, y1, z1);
		tes.addVertex(x1, y2, z1);
		tes.addVertex(x2, y2, z1);
		tes.addVertex(x2, y1, z1);

		tes.addVertex(x1, y1, z2);
		tes.addVertex(x2, y1, z2);
		tes.addVertex(x2, y2, z2);
		tes.addVertex(x1, y2, z2);

		tes.addVertex(x1, y1, z1);
		tes.addVertex(x1, y1, z2);
		tes.addVertex(x1, y2, z2);
		tes.addVertex(x1, y2, z1);

		tes.addVertex(x2, y1, z1);
		tes.addVertex(x2, y2, z1);
		tes.addVertex(x2, y2, z2);
		tes.addVertex(x2, y1, z2);

		tes.addVertex(x1, y1, z1);
		tes.addVertex(x2, y1, z1);
		tes.addVertex(x2, y1, z2);
		tes.addVertex(x1, y1, z2);

		tes.addVertex(x1, y2, z1);
		tes.addVertex(x1, y2, z2);
		tes.addVertex(x2, y2, z2);
		tes.addVertex(x2, y2, z1);
	}

	public static void disableLightmap() {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public static void enableLightmap() {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public static RGB getFogColor() {
		return new RGB(fogRed, fogGreen, fogBlue);
	}

	public static void registerFogUpdater() {
		MinecraftForge.EVENT_BUS.register(new FogColorUpdater());
	}

}
