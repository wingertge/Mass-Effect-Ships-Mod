package com.octagon.airships.client.gui.components;

import com.octagon.airships.reference.Textures;
import com.octagon.airships.sync.SyncableEnergyStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;
import openmods.gui.component.BaseComponent;
import openmods.gui.listener.IValueChangedListener;

import java.util.ArrayList;
import java.util.List;

public class GuiComponentEnergyGauge extends BaseComponent implements IValueChangedListener<Integer> {
    protected int width;
    protected int height;
    private SyncableEnergyStorage energyStorage;
    protected float progress;
    private GuiComponentTooltip tooltip;

    public GuiComponentEnergyGauge(int x, int y) {
        super(x, y);
        width = 14;
        height = 42;
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
        this.height = height;
        if(height < 2) height = 2;
        tooltip = new GuiComponentTooltip(x, y, x + width, y + height);
        valueChanged(0);
    }

    public void setWidth(int width) {

    }

    public void setEnergyStorage(SyncableEnergyStorage energyStorage) {
        if(this.energyStorage != null) {
            this.energyStorage.unsubscribe("energyStored", this);
            this.energyStorage.unsubscribe("maxEnergyStored", this);
        }
        this.energyStorage = energyStorage;
        if(energyStorage != null) {
            this.energyStorage.subscribe("energyStored", this);
            this.energyStorage.subscribe("maxEnergyStored", this);
        }
    }

    @Override
    public void render(Minecraft mc, int offsetX, int offsetY, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(Textures.Gui.MACHINE_ELEMENTS);

        //BACKGROUND
        drawTexturedModalRect(offsetX + x, offsetY + y, 0, 0, width, 1);
        drawTexturedModalRect(offsetX + x, offsetY + y + height - 1, 0, 3, width, 1);
        int remainingHeight = height - 2;
        if(remainingHeight % 2 != 0)
            drawTexturedModalRect(offsetX + x, offsetY + y + 1 - 1, 0, 2, width, 1);
        for(int i = 0; i < remainingHeight / 2; i++) {
            drawTexturedModalRect(offsetX + x, offsetY + y + 1 + i * 2 + (remainingHeight % 2), 0, 1, width, 2);
        }

        //PROGRESS
        if(progress == 0) return;
        int progressScaled = (int) (progress * (height - 2));
        int yStart = height - progressScaled - 1;

        if(progressScaled % 2 != 0)
            drawTexturedModalRect(offsetX + x, offsetY + y + yStart, 0, 5, width, 1);
        for(int i = 0; i < progressScaled / 2; i++) {
            drawTexturedModalRect(offsetX + x, offsetY + y + yStart + (progressScaled % 2) + (i * 2), 0, 4, width, 2);
        }
    }

    @Override
    public void renderOverlay(Minecraft mc, int offsetX, int offsetY, int mouseX, int mouseY) {
        tooltip.renderOverlay(mc, offsetX, offsetY, mouseX, mouseY);
    }

    @Override
    public void valueChanged(Integer value) {
        if(energyStorage == null) return;
        progress = (float)energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored();
        List<String> lines = new ArrayList<>();
        lines.add(StatCollector.translateToLocal("masseffectships.gui.powerLevel"));
        lines.add(StatCollector.translateToLocalFormatted("masseffectships.misc.energyStorage", energyStorage.getEnergyStored(), energyStorage.getMaxEnergyStored()));
        tooltip.setText(lines);
    }
}
