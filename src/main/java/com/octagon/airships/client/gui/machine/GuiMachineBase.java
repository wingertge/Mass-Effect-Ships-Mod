package com.octagon.airships.client.gui.machine;

import com.octagon.airships.block.tileentity.TileEntityMachineBase;
import com.octagon.airships.client.gui.GuiAirshipsContainer;
import com.octagon.airships.client.gui.components.GuiComponentProgressBar;
import com.octagon.airships.client.gui.components.GuiComponentTooltip;
import com.octagon.airships.reference.Textures;
import com.octagon.airships.util.LangUtil;
import javafx.geometry.Orientation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.gui.component.BaseComposite;

import java.util.ArrayList;
import java.util.List;

public class GuiMachineBase<T extends ContainerMachineBase<? extends TileEntityMachineBase>> extends GuiAirshipsContainer<T> {
    public GuiMachineBase(T container, String title) {
        super(container, 176, 166, title);

        final TileEntityMachineBase machine = container.getOwner();

        final GuiComponentProgressBar energyLevel = new GuiComponentProgressBar(9, 8, 14, 42, Orientation.VERTICAL, false);
        energyLevel.setActiveTexture(Textures.Gui.MACHINE_ELEMENTS, 0, 0);
        energyLevel.setBackgroundTexture(Textures.Gui.MACHINE_ELEMENTS, 14, 0);
        energyLevel.setProgress((float)machine.getEnergyStored(ForgeDirection.NORTH) / (float)machine.getMaxEnergyStored(ForgeDirection.NORTH));
        root.addComponent(energyLevel);

        final GuiComponentTooltip tooltip = new GuiComponentTooltip(10, 9, 22, 49);
        final List<String> lines = new ArrayList<>();
        lines.add(LangUtil.localize("gui.powerLevel", true));
        lines.add(StatCollector.translateToLocalFormatted("masseffectships.misc.energyStorage", machine.getEnergyStored(null), machine.getMaxEnergyStored(null)));
        tooltip.setText(lines);
        root.addComponent(tooltip);

        machine.subscribeListener("energyStored", value -> updateDisplayElements(machine, tooltip, energyLevel));

        machine.subscribeListener("maxEnergyStored", value -> updateDisplayElements(machine, tooltip, energyLevel));

        dispatcher().triggerAll();
    }

    public GuiMachineBase(T container) {
        this(container, "masseffectships.gui.machineBase");
    }

    private void updateDisplayElements(TileEntityMachineBase machine, GuiComponentTooltip tooltip, GuiComponentProgressBar energyLevel) {
        List<String> newLines = new ArrayList<>();
        newLines.add(LangUtil.localize("gui.powerLevel", true));
        newLines.add(StatCollector.translateToLocalFormatted("masseffectships.misc.energyStorage", machine.getEnergyStored(null), machine.getMaxEnergyStored(null)));
        tooltip.setText(newLines);
        float progress = (float)machine.getEnergyStored(ForgeDirection.NORTH) / (float)machine.getMaxEnergyStored(ForgeDirection.NORTH);
        energyLevel.setProgress(progress);
    }

    public void init(BaseComposite root) {

    }
}
