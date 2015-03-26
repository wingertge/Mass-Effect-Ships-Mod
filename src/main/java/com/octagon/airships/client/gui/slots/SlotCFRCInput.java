package com.octagon.airships.client.gui.slots;

import com.octagon.airships.init.ModItems;
import com.octagon.airships.item.ItemCFRCPlate;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotCFRCInput extends SlotInput {
    public SlotCFRCInput(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);

        setBackgroundIcon(ItemCFRCPlate.Icons.slotIcon);
    }

    @Override
    public boolean isItemValid(ItemStack item) {
        return item.getItem().equals(ModItems.cfrcPlate);
    }
}
