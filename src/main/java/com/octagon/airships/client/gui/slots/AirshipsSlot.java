package com.octagon.airships.client.gui.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public abstract class AirshipsSlot extends Slot {
    protected AirshipsSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
    }
}
