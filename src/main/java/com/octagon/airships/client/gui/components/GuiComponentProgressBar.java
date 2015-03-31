package com.octagon.airships.client.gui.components;

import javafx.geometry.Orientation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import openmods.api.IValueReceiver;
import openmods.gui.component.BaseComponent;

public class GuiComponentProgressBar extends BaseComponent {
    private float progress;
    private int width;
    private int height;
    private Orientation orientation;
    private boolean invert;
    private ResourceLocation activeTexture = null;
    private ResourceLocation backgroundTexture = null;
    private int activeTextureX;
    private int activeTextureY;
    private int backgroundTextureX;
    private int backgroundTextureY;
    private IIcon backgroundIcon = null;
    private IIcon activeIcon = null;

    public GuiComponentProgressBar(int x, int y, int width, int height, Orientation orientation, boolean invert) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.orientation = orientation;
        this.invert = invert;
    }

    public GuiComponentProgressBar(int x, int y) {
        super(x, y);
    }

    public void setActiveTexture(ResourceLocation texture, int x, int y) {
        this.activeTexture = texture;
        activeTextureX = x;
        activeTextureY = y;
    }

    public void setBackgroundTexture(ResourceLocation texture, int x, int y) {
        this.backgroundTexture = texture;
        backgroundTextureX = x;
        backgroundTextureY = y;
    }

    public void setActiveTexture(IIcon icon) {
        activeIcon = icon;
    }

    public void setBackgroundTexture(IIcon icon) {
        backgroundIcon = icon;
    }

    @Override
    public void render(Minecraft mc, int offsetX, int offsetY, int mouseX, int mouseY) {
        if(backgroundIcon != null) {
            drawTexturedModelRectFromIcon(offsetX + x, offsetY + y, backgroundIcon, width, height);
        }

        if(backgroundTexture != null) {
            mc.getTextureManager().bindTexture(backgroundTexture);

            drawTexturedModalRect(offsetX + x, offsetY + y, backgroundTextureX, backgroundTextureY, width, height);
        }

        /*if(activeIcon != null) {
            switch (orientation) {
                case HORIZONTAL:
                    int progressScaledHor = Math.round(width * progress);
                    if(!invert) drawTexturedModelRectFromIcon(offsetX + x, offsetY + y, activeIcon, progressScaledHor, height);
                    else drawTexturedModelRectFromIcon(offsetX + x + progressScaledHor, offsetY + y, activeIcon, progressScaledHor, height);
                    return;
                case VERTICAL:
                    int progressScaledVer = Math.round(height * progress);
                    if(!invert) drawTexturedModelRectFromIcon(offsetX + x, offsetY + y + height - progressScaledVer, activeIcon, width, progressScaledVer);
                    else drawTexturedModelRectFromIcon(offsetX + x, offsetY + y, activeIcon, width, progressScaledVer);
            }
        }*/
        if (activeIcon != null) {
            int posX = offsetX + x;
            int posY = offsetY + y;
            int newWidth = width;
            int newHeight = height;

            final float minU = activeIcon.getMinU();
            final float maxU = activeIcon.getMaxU();

            final float minV = activeIcon.getMinV();
            final float maxV = activeIcon.getMaxV();

            if(orientation == Orientation.HORIZONTAL) {
                if(invert)
                    posX += width - (width * progress);
                newWidth -= width * progress;
            } else {
                if(!invert)
                    posY += height - (height * progress);
                newHeight -= height * progress;
            }

            mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setColorOpaque_F(1, 1, 1);

            tessellator.addVertexWithUV(posX, posY + newHeight, this.zLevel, minU, maxV);
            tessellator.addVertexWithUV(posX + newWidth, posY + newHeight, this.zLevel, maxU, maxV);
            tessellator.addVertexWithUV(posX + newWidth, posY, this.zLevel, maxU, minV);
            tessellator.addVertexWithUV(posX, posY, this.zLevel, minU, minV);
            tessellator.draw();
        }

        if(activeTexture != null) {
            mc.getTextureManager().bindTexture(activeTexture);

            switch (orientation) {
                case HORIZONTAL:
                    int progressScaledHor = Math.round(width * progress);
                    if(!invert) drawTexturedModalRect(offsetX + x, offsetY + y, activeTextureX, activeTextureY, progressScaledHor, height);
                    else drawTexturedModalRect(offsetX + x + progressScaledHor, offsetY + y, activeTextureX + width - progressScaledHor, activeTextureY, progressScaledHor, height);
                    return;
                case VERTICAL:
                    int progressScaledVer = Math.round(height * progress);
                    if(!invert) drawTexturedModalRect(offsetX + x, offsetY + y + height - progressScaledVer, activeTextureX, activeTextureY + height - progressScaledVer, width, progressScaledVer);
                    else drawTexturedModalRect(offsetX + x, offsetY + y, activeTextureX, activeTextureY, width, progressScaledVer);
            }
        }

        zLevel = 0;
    }

    @Override
    public void renderOverlay(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {}

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public IValueReceiver<Integer> progressReceiver() {
        return value -> progress = value;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public void setOrientation(String value) {
        this.orientation = value.equalsIgnoreCase("horizontal") ? Orientation.HORIZONTAL : Orientation.VERTICAL;
    }
}
