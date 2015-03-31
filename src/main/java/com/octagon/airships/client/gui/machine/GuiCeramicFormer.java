package com.octagon.airships.client.gui.machine;

import com.octagon.airships.block.tileentity.TileEntityCeramicFormer;
import com.octagon.airships.client.gui.components.GuiComponentProgressBar;
import com.octagon.airships.reference.Textures;
import javafx.geometry.Orientation;

public class GuiCeramicFormer extends GuiMachineBase<ContainerCeramicFormer> {
    public GuiCeramicFormer(ContainerCeramicFormer container) {
        super(container, "masseffectships.gui.ceramicFormer");

        TileEntityCeramicFormer machine = container.getOwner();

        final GuiComponentProgressBar progress = new GuiComponentProgressBar(81, 25, 24, 29, Orientation.HORIZONTAL, false);
        progress.setActiveTexture(Textures.Gui.MACHINE_ELEMENTS, 68, 0);
        progress.setBackgroundTexture(Textures.Gui.MACHINE_ELEMENTS, 44, 0);
        progress.setProgress((float) machine.getCurrentWork() / (float) machine.getMaxWork());
        root.addComponent(progress);

        machine.subscribeListener("currentWork", value -> progress.setProgress((float)machine.getCurrentWork() / (float)machine.getMaxWork()));
        machine.subscribeListener("maxWork", value -> progress.setProgress((float)machine.getCurrentWork() / (float)machine.getMaxWork()));

        dispatcher().triggerAll();
    }
}
