package com.octagon.airships.client.render.tileentity;

import com.octagon.airships.block.BlockEezoCoreBase.Icons;
import com.octagon.airships.block.tileentity.TileEntityEezoCoreMultiblock;
import com.octagon.airships.client.render.model.ModelSphere;
import com.octagon.airships.init.ModBlocks;
import com.octagon.airships.reference.Textures;
import com.octagon.airships.util.render.DisplayListWrapper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import openmods.utils.Coord;
import openmods.utils.TextureUtils;
import org.lwjgl.opengl.GL11;

import java.util.Collection;

public class TileEntityEezoCoreBaseRenderer extends TileEntitySpecialRenderer {
    private static final ModelSphere model = new ModelSphere();

    private final DisplayListWrapper wrapper = new DisplayListWrapper() {
        @Override
        public void compile() {
            Tessellator t = Tessellator.instance;
            RenderBlocks renderBlocks = new RenderBlocks();
            renderBlocks.setRenderBounds(0.05D, 0.05D, 0.05D, 0.95D, 0.95D, 0.95D);
            t.startDrawingQuads();
            t.setBrightness(200);
            renderBlocks.renderFaceXNeg(ModBlocks.eezoCoreBase, -0.5D, 0.0D, -0.5D, Icons.marker);
            renderBlocks.renderFaceXPos(ModBlocks.eezoCoreBase, -0.5D, 0.0D, -0.5D, Icons.marker);
            renderBlocks.renderFaceYNeg(ModBlocks.eezoCoreBase, -0.5D, 0.0D, -0.5D, Icons.marker);
            renderBlocks.renderFaceYPos(ModBlocks.eezoCoreBase, -0.5D, 0.0D, -0.5D, Icons.marker);
            renderBlocks.renderFaceZNeg(ModBlocks.eezoCoreBase, -0.5D, 0.0D, -0.5D, Icons.marker);
            renderBlocks.renderFaceZPos(ModBlocks.eezoCoreBase, -0.5D, 0.0D, -0.5D, Icons.marker);
            t.draw();
        }
    };

    public TileEntityEezoCoreBaseRenderer() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTextureChange(TextureStitchEvent event) {
        if (event.map.getTextureType() == 0) wrapper.reset();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
        TileEntityEezoCoreMultiblock multiblock = (TileEntityEezoCoreMultiblock)tileentity;

        if(multiblock.isComplete()) {
            GL11.glPushMatrix();

            int diameter = multiblock.getRadius() * 2 + 1;
            GL11.glTranslated(x - multiblock.getRadius(), y + ((multiblock.getRadius() * 2 + 1) * 0.106), z + multiblock.getRadius() + 1);
            //GL11.glScaled(diameter, diameter, diameter);
            GL11.glScaled(diameter * 0.00156493134906993521705858633492d, diameter * 0.00160472945866056441544520009372d, diameter * 0.00156486196352619735413139206991d);

            bindTexture(Textures.Model.CORE_BASE);
            model.renderPart("base");

            bindTexture(Textures.Model.CORE_MID);
            model.renderPart("mid");

            if (multiblock.isActive())
            {
                this.bindTexture(Textures.Model.CORE_ACTIVE);
            }
            else
            {
                this.bindTexture(Textures.Model.CORE_IDLE);
            }

            model.renderPart("core");

            /*model.renderPart("sphere");

            bindTexture(Textures.Model.CORE_BOTTOM);

            model.renderPart("bottom");*/
            GL11.glPopMatrix();
        } else if (multiblock.shouldRenderGuide()) {
            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);
            float scaleDelta = multiblock.getTimeSinceChange();
            renderShape(multiblock, multiblock.getShape(), multiblock.getColor(), scaleDelta);
            if (scaleDelta < 1.0) {
                renderShape(multiblock, multiblock.getShape(), multiblock.getColor(), 1.0f - scaleDelta);
            }
            GL11.glPopMatrix();
        }
    }

    private void renderShape(TileEntityEezoCoreMultiblock tileEntity, Collection<Coord> shape, int color, float scale) {
        if (shape == null) return;

        TextureUtils.bindDefaultTerrainTexture();

        GL11.glColor3ub((byte) (color >> 16), (byte) (color >> 8), (byte) (color >> 0));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_LIGHTING);

        for (Coord coord : shape)
            renderAt(coord.x, coord.y + tileEntity.getRadius(), coord.z, scale);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void renderAt(double x, double y, double z, float scale) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5F, y, z + 0.5F);
        GL11.glScalef(scale, scale, scale);
        wrapper.render();
        GL11.glPopMatrix();
    }
}
