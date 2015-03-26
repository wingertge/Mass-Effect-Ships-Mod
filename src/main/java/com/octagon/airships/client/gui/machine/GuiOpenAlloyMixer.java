package com.octagon.airships.client.gui.machine;

import com.octagon.airships.block.tileentity.TileEntityAlloyMixer;
import com.octagon.airships.client.gui.components.GuiComponentProgressBar;
import com.octagon.airships.reference.Textures;
import javafx.geometry.Orientation;

public class GuiOpenAlloyMixer extends GuiOpenMachineBase<ContainerAlloyMixer> {
    public GuiOpenAlloyMixer(ContainerAlloyMixer container) {
        super(container, "masseffectships.gui.alloyMixer");

        TileEntityAlloyMixer machine = container.getOwner();

        final GuiComponentProgressBar progressBar = new GuiComponentProgressBar(81, 35, 16, 15, Orientation.VERTICAL, true);
        progressBar.setActiveTexture(Textures.Gui.MACHINE_ELEMENTS, 28, 0);
        progressBar.setBackgroundTexture(Textures.Gui.MACHINE_ELEMENTS, 28, 15);
        progressBar.setProgress((float) machine.getCurrentWork() / (float)machine.getMaxWork());
        root.addComponent(progressBar);

        machine.subscribeListener("currentWork", (Integer value) -> progressBar.setProgress((float) machine.getCurrentWork() / (float)machine.getMaxWork()));

        dispatcher().triggerAll();
    }
}
