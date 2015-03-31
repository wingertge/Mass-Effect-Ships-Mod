package com.octagon.airships.client.gui.components;

import com.octagon.airships.reference.Colors;
import com.octagon.airships.reference.Textures;
import com.octagon.airships.sync.MonitoredTank;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import openmods.gui.component.BaseComponent;
import openmods.gui.listener.IValueChangedListener;

import java.util.ArrayList;
import java.util.List;

public class GuiComponentFluidGauge extends BaseComponent implements IValueChangedListener<FluidStack> {
    private int width;
    private int height;
    private MonitoredTank tank;
    private GuiComponentTooltip tooltip;
    private float progress;
    private FluidStack fluid;

    public GuiComponentFluidGauge(int x, int y) {
        super(x, y);
        width = 14;
        height = 30;
        tooltip = new GuiComponentTooltip(x, y, x + width, y + height);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if(height < 4) height = 4;
        this.height = height;
        tooltip = new GuiComponentTooltip(x, y, x + width, y + height);
        valueChanged(fluid);
    }

    public void setWidth(int width) {
        if(width < 4) width = 4;
        this.width = width;
        tooltip = new GuiComponentTooltip(x, y, x + width, y + height);
        valueChanged(fluid);
    }

    public void setHasTooltip(boolean hasTooltip) {
        if(hasTooltip)
            tooltip = new GuiComponentTooltip(x, y, x + width, y + height);
        else tooltip = null;
    }

    @Override
    public void render(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
        //Gray Background
        drawRect(offsetX + x, offsetY + y, offsetX + x + width, offsetY + y + height, Colors.DARK_GRAY);

        //Fluid Level
        if(fluid != null) {

        }

        minecraft.getTextureManager().bindTexture(Textures.Gui.MACHINE_ELEMENTS);

        //Foreground
        //right side
        int xStart = width - 3;
        int newHeight = height - 2;
        for(int i = 0; i < newHeight / 2; i++) {
            drawTexturedModalRect(offsetX + x + xStart, offsetY + y + 1 + (newHeight % 2) + (i * 2), 1, 7, 3, 2);
        }
        //top border
        xStart = width - 4;
        drawTexturedModalRect(offsetX + x + xStart, offsetY + y, 0, 6, 4, 1);
        for(int i = 0; i < xStart; i++) {
            drawTexturedModalRect(offsetX + x + i, offsetY + y, 0, 6, 1, 1);
        }
        //bottom border
        drawTexturedModalRect(offsetX + x, offsetY + y + height - 1, 0, 9, 4, 1);
        for(int i = 4; i < xStart + 4; i++) {
            drawTexturedModalRect(offsetX + x + i, offsetY + y + height - 1, 1, 9, 1, 1);
        }
        //left border
        if(newHeight % 2 != 0) drawTexturedModalRect(offsetX + x, offsetY + y + 1, 0, 7, 1, 1);
        for(int i = 0; i < newHeight / 2; i++) {
            drawTexturedModalRect(offsetX + x, offsetY + y + 1 + (newHeight % 2) + (i * 2), 0, 7, 1, 2);
        }
    }

    @Override
    public void renderOverlay(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY) {
        if(tooltip != null)
            tooltip.renderOverlay(minecraft, offsetX, offsetY, mouseX, mouseY);
    }

    public void setTank(MonitoredTank tank) {
        if(this.tank != null) this.tank.unsubscribe(this);
        this.tank = tank;
        this.tank.subscribe(this);
    }

    public GuiComponentTooltip getTooltip() {
        return tooltip;
    }

    @Override
    public void valueChanged(FluidStack value) {
        if(tank == null) return;
        fluid = tank.getFluid();
        if(fluid != null) progress = (float) fluid.amount / tank.getCapacity();
        else progress = 0;

        if(tooltip != null) {
            List<String> lines = new ArrayList<>();
            lines.add(StatCollector.translateToLocal("masseffectships.gui.fluidLevel"));
            lines.add(StatCollector.translateToLocalFormatted("masseffectships.misc.fluidStorage", fluid != null ? fluid.getLocalizedName() : StatCollector.translateToLocal("masseffectships.misc.empty"),
                    fluid != null ? fluid.amount : 0, tank.getCapacity()));

            tooltip.setText(lines);
        }
    }
}
