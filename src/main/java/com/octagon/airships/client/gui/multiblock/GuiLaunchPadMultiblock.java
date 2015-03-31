package com.octagon.airships.client.gui.multiblock;

import com.octagon.airships.block.tileentity.TileEntityLaunchPadMultiblock;
import com.octagon.airships.client.gui.components.GuiComponentProgressBar;
import com.octagon.airships.client.gui.components.GuiComponentTooltip;
import com.octagon.airships.reference.Textures;
import javafx.geometry.Orientation;
import net.minecraft.util.StatCollector;
import openmods.gui.SyncedGuiContainer;

import java.util.ArrayList;
import java.util.List;

public class GuiLaunchPadMultiblock extends SyncedGuiContainer<ContainerLaunchPadMultiblock> {
    public GuiLaunchPadMultiblock(ContainerLaunchPadMultiblock container) {
        super(container, 176, 166, "");

        TileEntityLaunchPadMultiblock multiblock = container.getOwner();

        final GuiComponentProgressBar energyBar = new GuiComponentProgressBar(8, 8, 14, 50, Orientation.VERTICAL, false);
        energyBar.setBackgroundTexture(Textures.Gui.MACHINE_ELEMENTS, 0, 60);
        energyBar.setActiveTexture(Textures.Gui.MACHINE_ELEMENTS, 14, 60);
        root.addComponent(energyBar);

        final GuiComponentTooltip energyBarTooltip = new GuiComponentTooltip(8, 8, 21, 57);
        updateEnergyBar(energyBar, energyBarTooltip, multiblock);
        root.addComponent(energyBarTooltip);

        /*final GuiComponentTankLevel tank1Level = new GuiComponentTankLevel(29, 9, 14, 24, multiblock.getTank(1).getCapacity());
        addSyncUpdateListener(ValueCopyAction.create(multiblock.getTank(1), tank1Level.fluidReceiver()));
        root.addComponent(tank1Level);

        final GuiComponentTankLevel tank2Level = new GuiComponentTankLevel(29, 35, 14, 24, multiblock.getTank(2).getCapacity());
        addSyncUpdateListener(ValueCopyAction.create(multiblock.getTank(2), tank2Level.fluidReceiver()));
        root.addComponent(tank2Level);

        final GuiComponentTankLevel outputTankLevel = new GuiComponentTankLevel(49, 9, 14, 30, multiblock.getTank(0).getCapacity());
        addSyncUpdateListener(ValueCopyAction.create(multiblock.getTank(0), outputTankLevel.fluidReceiver()));
        root.addComponent(outputTankLevel);*/

        /*final GuiComponentFluidGauge tank1 = new GuiComponentFluidGauge(multiblock.getTank(1), 28, 8, 14, 24, true);
        tank1.setBackgroundTexture(Textures.Gui.MACHINE_ELEMENTS, 28, 30);
        tank1.setActiveTexture(Textures.Gui.MACHINE_ELEMENTS, 42, 30);
        root.addComponent(tank1);

        final GuiComponentFluidGauge tank2 = new GuiComponentFluidGauge(multiblock.getTank(2), 28, 34, 14, 24, true);
        tank2.setBackgroundTexture(Textures.Gui.MACHINE_ELEMENTS, 28, 30);
        tank2.setActiveTexture(Textures.Gui.MACHINE_ELEMENTS, 42, 30);
        root.addComponent(tank2);

        final GuiComponentFluidGauge outputTank = new GuiComponentFluidGauge(multiblock.getTank(0), 48, 8, 14, 30, true);
        outputTank.setBackgroundTexture(Textures.Gui.MACHINE_ELEMENTS, 28, 54);
        outputTank.setActiveTexture(Textures.Gui.MACHINE_ELEMENTS, 42, 54);
        root.addComponent(outputTank);


        multiblock.getEnergyStorage().subscribe("energyStored", a -> updateEnergyBar(energyBar, energyBarTooltip, multiblock));
        multiblock.getEnergyStorage().subscribe("maxEnergyStored", a -> updateEnergyBar(energyBar, energyBarTooltip, multiblock));*/
    }

    private void updateEnergyBar(GuiComponentProgressBar energyBar, GuiComponentTooltip tooltip, TileEntityLaunchPadMultiblock multiblock) {
        energyBar.setProgress((float) multiblock.getEnergyStored(null) / multiblock.getMaxEnergyStored(null));
        List<String> lines = new ArrayList<>();
        lines.add(StatCollector.translateToLocal("masseffectships.gui.powerLevel"));
        lines.add(StatCollector.translateToLocalFormatted("masseffectships.misc.energyStorage", multiblock.getEnergyStored(null), multiblock.getMaxEnergyStored(null)));
        tooltip.setText(lines);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        postRender(mouseX, mouseY);
    }
}
