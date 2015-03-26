package com.octagon.airships.client.gui.slots;

import com.octagon.airships.recipe.CeramicFormerRecipe;
import com.octagon.airships.recipe.RecipesCeramicFormer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotCeramicFormerInput extends SlotInput {
    public SlotCeramicFormerInput(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
    }

    @Override
    public boolean isItemValid(ItemStack item) {
        return RecipesCeramicFormer.isItemValid(item);
    }
}
