package com.octagon.airships.client.gui.multiblock;

import com.octagon.airships.client.gui.components.GuiComponentProgressBar;
import com.octagon.airships.reference.Textures;
import javafx.geometry.Orientation;
import net.minecraftforge.fluids.FluidStack;
import openmods.gui.BaseGuiContainer;

public class GuiLaunchPadMultiblock extends BaseGuiContainer<ContainerLaunchPadMultiblock> {
    public GuiLaunchPadMultiblock(ContainerLaunchPadMultiblock container) {
        super(container, 176, 166, "");

        final GuiComponentProgressBar energyBar = new GuiComponentProgressBar(8, 8, 14, 50, Orientation.VERTICAL, false);
        energyBar.setBackgroundTexture(Textures.Gui.MACHINE_ELEMENTS, 0, 60);
        energyBar.setActiveTexture(Textures.Gui.MACHINE_ELEMENTS, 14, 60);
        root.addComponent(energyBar);

        FluidStack stack1 = container.getOwner().getFluidProvider1().getValue();
        FluidStack stack2 = container.getOwner().getFluidProvider2().getValue();

        final GuiComponentProgressBar tank1 = new GuiComponentProgressBar(28, 8, 14, 24, Orientation.VERTICAL, false);
        tank1.setBackgroundTexture(Textures.Gui.MACHINE_ELEMENTS, 28, 30);
        if(stack1 != null)
            tank1.setActiveTexture(stack1.getFluid().getStillIcon());
        else tank1.setActiveTexture(Textures.Gui.MACHINE_ELEMENTS, 28, 30);
        root.addComponent(tank1);

        final GuiComponentProgressBar tank2 = new GuiComponentProgressBar(28, 34, 14, 24, Orientation.VERTICAL, false);
        tank2.setBackgroundTexture(Textures.Gui.MACHINE_ELEMENTS, 28, 30);
        if(stack2 != null)
            tank2.setActiveTexture(stack2.getFluid().getStillIcon());
        else tank2.setActiveTexture(Textures.Gui.MACHINE_ELEMENTS, 28, 30);
        root.addComponent(tank2);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        postRender(mouseX, mouseY);
    }
}
