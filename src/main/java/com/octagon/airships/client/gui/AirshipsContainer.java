package com.octagon.airships.client.gui;

import com.octagon.airships.client.gui.slots.AirshipsSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import openmods.container.ContainerBase;
import openmods.tileentity.SyncedTileEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class AirshipsContainer<T extends SyncedTileEntity> extends ContainerBase<T> {
    protected AirshipsContainer(IInventory playerInventory, IInventory ownerInventory, T owner) {
        super(playerInventory, ownerInventory, owner);
    }

    public List<AirshipsSlot> activeInventorySlots = new ArrayList<>();

    @Override
    public boolean canInteractWith (EntityPlayer entityplayer)
    {
        return false;
    }

    protected AirshipsSlot addDualSlotToContainer (AirshipsSlot slot)
    {
        slot.activeSlotNumber = this.activeInventorySlots.size();
        this.activeInventorySlots.add(slot);
        this.addSlotToContainer(slot);
        return slot;
    }
}
