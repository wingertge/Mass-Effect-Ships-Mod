package com.octagon.airships.client.gui.slots;

import com.octagon.airships.item.ItemProbe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotProbe extends AirshipsSlot {
    public SlotProbe(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof ItemProbe && ((ItemProbe) stack.getItem()).getType() != ItemProbe.Type.EMPTY;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }
}
