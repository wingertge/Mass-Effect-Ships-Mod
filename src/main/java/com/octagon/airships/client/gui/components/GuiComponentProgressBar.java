package com.octagon.airships.client.gui.components;

import com.octagon.airships.util.LogHelper;
import javafx.geometry.Orientation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import openmods.api.IValueReceiver;
import openmods.gui.component.BaseComponent;

public class GuiComponentProgressBar extends BaseComponent {
    private float progress;
    private final int width;
    private final int height;
    private final Orientation orientation;
    private final boolean invert;
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
        if(backgroundTexture != null) {
            mc.getTextureManager().bindTexture(backgroundTexture);

            drawTexturedModalRect(offsetX + x, offsetY + y, backgroundTextureX, backgroundTextureY, width, height);
        } else if(backgroundIcon != null) {
            drawTexturedModelRectFromIcon(offsetX + x, offsetY + y, backgroundIcon, width, height);
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
        } else if(activeIcon != null) {
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
        }
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
        if(progress == 0)
            LogHelper.info("test");
    }

    public IValueReceiver<Integer> progressReceiver() {
        return value -> progress = value;
    }
}
