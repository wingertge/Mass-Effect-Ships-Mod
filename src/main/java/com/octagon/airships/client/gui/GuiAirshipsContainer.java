package com.octagon.airships.client.gui;

import com.octagon.airships.client.gui.slots.AirshipsSlot;
import net.minecraft.inventory.Slot;
import openmods.container.ContainerBase;
import openmods.gui.SyncedGuiContainer;
import openmods.sync.ISyncMapProvider;

public abstract class GuiAirshipsContainer<T extends ContainerBase<? extends ISyncMapProvider>> extends SyncedGuiContainer<T> {

    @SuppressWarnings({"unchecked"})
    protected GuiAirshipsContainer(T container, int width, int height, String name) {
        super(container, width, height, name);
    }

    @Override
    public void func_146977_a (Slot slot)
    {
        if (!(slot instanceof AirshipsSlot) || ((AirshipsSlot) slot).getActive())
        {
            super.func_146977_a(slot);
        }
    }

    @Override
    public boolean isMouseOverSlot (Slot slot, int mouseX, int mouseY) {
        return (!(slot instanceof AirshipsSlot) || ((AirshipsSlot) slot).getActive()) && super.isMouseOverSlot(slot, mouseX, mouseY);
    }
}
