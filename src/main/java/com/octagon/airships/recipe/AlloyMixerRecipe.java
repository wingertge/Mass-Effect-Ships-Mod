package com.octagon.airships.recipe;

import net.minecraft.item.ItemStack;

public class AlloyMixerRecipe {
    private ItemStack[] recipeItems;
    private final ItemStack results;

    public AlloyMixerRecipe(ItemStack item1, ItemStack item2, ItemStack item3, ItemStack results) {
        recipeItems = new ItemStack[3];
        recipeItems[0] = item1;
        recipeItems[1] = item2;
        recipeItems[2] = item3;
        this.results = results;
    }

    public int getItemPosition(ItemStack itemStack) {
        for(int i = 0; i < recipeItems.length; i++) {
            if(recipeItems[i].getItem().equals(itemStack.getItem()) && recipeItems[i].stackSize <= itemStack.stackSize)
                return i;
        }
        return -1;
    }

    public int getItemPositionIgnoreAmount(ItemStack itemStack) {
        for(int i = 0; i < recipeItems.length; i++) {
            if(recipeItems[i].getItem().equals(itemStack.getItem()))
                return i;
        }
        return -1;
    }

    public ItemStack getResults() {
        return results.copy();
    }

    public ItemStack getItemAt(int i) {
        return recipeItems[i];
    }
}
