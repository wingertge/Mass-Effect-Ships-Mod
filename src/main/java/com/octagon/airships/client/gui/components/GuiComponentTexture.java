package com.octagon.airships.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import openmods.gui.component.BaseComponent;

public class GuiComponentTexture extends BaseComponent {


    private ResourceLocation texture;
    private int xStart;
    private int yStart;
    private int width;
    private int height;

    public GuiComponentTexture(ResourceLocation texture, int x, int y, int xStart, int yStart, int width, int height) {
        super(x, y);
        this.texture = texture;
        this.xStart = xStart;
        this.yStart = yStart;
        this.width = width;
        this.height = height;
    }

    public void setTexture(ResourceLocation texture, int xStart, int yStart) {
        this.texture = texture;
        this.xStart = xStart;
        this.yStart = yStart;
    }

    public void setTexture(ResourceLocation texture, int xStart, int yStart, int width, int height) {
        this.texture = texture;
        this.xStart = xStart;
        this.yStart = yStart;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void render(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {

    }

    @Override
    public void renderOverlay(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(offsetX + x, offsetY + y, xStart, yStart, width, height);
    }
}
