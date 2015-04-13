package com.octagon.airships.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import openmods.gui.component.BaseComponent;

import java.util.ArrayList;
import java.util.List;

public class GuiComponentTooltip extends BaseComponent {

    private int xMin = 0;
    private int yMin = 0;
    private int xMax = 0;
    private int yMax = 0;
    private int width;
    private int height;
    private FontRenderer fontRenderer;
    private List<String> text = new ArrayList<String>();

    public GuiComponentTooltip(int xMin, int yMin, int xMax, int yMax) {
        super(xMin, yMin);
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
        fontRenderer = Minecraft.getMinecraft().fontRenderer;
    }

    @Override
    public int getWidth() {
        return xMax - xMin;
    }

    @Override
    public int getHeight() {
        return yMax - yMin;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    @Override
    public void render(Minecraft mc, int offsetX, int offsetY, int mouseX, int mouseY) {

    }

    @Override
    public void renderOverlay(Minecraft mc, int offsetX, int offsetY, int mouseX, int mouseY) {
        if(mouseX >= xMin && mouseX <= xMax && mouseY >= yMin && mouseY <= yMax) {
            x = mouseX;
            y = mouseY;
            drawHoveringText(text, mouseX + offsetX, mouseY + offsetY, fontRenderer);
        }
    }

    public void setFontRenderer(FontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
    }
}
