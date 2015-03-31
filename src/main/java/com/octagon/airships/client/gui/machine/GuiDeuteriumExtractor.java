package com.octagon.airships.client.gui.machine;

import com.octagon.airships.block.tileentity.TileEntityDeuteriumExtractor;
import com.octagon.airships.client.gui.GuiAirshipsContainer;
import com.octagon.airships.client.gui.components.GuiComponentEnergyGauge;
import com.octagon.airships.client.gui.components.GuiComponentFluidGauge;
import com.octagon.airships.client.gui.components.GuiComponentProgressBar;
import com.octagon.airships.reference.Textures;
import javafx.geometry.Orientation;

public class GuiDeuteriumExtractor extends GuiAirshipsContainer<ContainerDeuteriumExtractor> {
    public GuiDeuteriumExtractor(ContainerDeuteriumExtractor container) {
        super(container, 176, 166, "deuteriumExtractor");

        TileEntityDeuteriumExtractor machine = container.getOwner();

        GuiComponentEnergyGauge energyBar = new GuiComponentEnergyGauge(9, 8);
        energyBar.setHeight(42);
        energyBar.setEnergyStorage(machine.getEnergyStorage());
        root.addComponent(energyBar);

        final GuiComponentFluidGauge inputTank = new GuiComponentFluidGauge(63, 8);
        inputTank.setHeight(42);
        inputTank.setTank(machine.getInputTank());
        root.addComponent(inputTank);

        final GuiComponentFluidGauge outputTank = new GuiComponentFluidGauge(117, 8);
        outputTank.setHeight(42);
        outputTank.setTank(machine.getOutputTank());
        root.addComponent(outputTank);
        root.addComponent(inputTank.getTooltip());

        final GuiComponentProgressBar craftingProgress = new GuiComponentProgressBar(84, 16, 26, 26, Orientation.HORIZONTAL, false);
        craftingProgress.setBackgroundTexture(Textures.Gui.MACHINE_ELEMENTS, 56, 29);
        craftingProgress.setActiveTexture(Textures.Gui.MACHINE_ELEMENTS, 82, 29);
        craftingProgress.setProgress((float) machine.getCurrentWork().get() / machine.getMaxWork().get());
        root.addComponent(craftingProgress);

        machine.getCurrentWork().subscribe(a -> craftingProgress.setProgress((float) machine.getCurrentWork().get() / machine.getMaxWork().get()));
        machine.getMaxWork().subscribe(a -> craftingProgress.setProgress((float)machine.getCurrentWork().get() / machine.getMaxWork().get()));
    }
}
