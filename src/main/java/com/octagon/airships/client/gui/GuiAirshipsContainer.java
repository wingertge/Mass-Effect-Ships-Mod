package com.octagon.airships.client.gui;

import openmods.container.ContainerBase;
import openmods.gui.SyncedGuiContainer;
import openmods.sync.ISyncMapProvider;

public abstract class GuiAirshipsContainer<T extends ContainerBase<? extends ISyncMapProvider>> extends SyncedGuiContainer<T> {

    @SuppressWarnings({"unchecked"})
    protected GuiAirshipsContainer(T container, int width, int height, String name) {
        super(container, width, height, name);
    }
}
