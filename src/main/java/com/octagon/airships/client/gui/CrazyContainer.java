package com.octagon.airships.client.gui;

import net.minecraft.inventory.IInventory;
import openmods.tileentity.SyncedTileEntity;

public abstract class CrazyContainer<T extends SyncedTileEntity> extends AirshipsContainer<T> {
    protected CrazyContainer(IInventory playerInventory, IInventory ownerInventory, T owner) {
        super(playerInventory, ownerInventory, owner);
    }
}
