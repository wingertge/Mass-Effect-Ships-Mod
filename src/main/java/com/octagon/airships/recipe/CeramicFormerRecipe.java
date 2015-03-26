package com.octagon.airships.recipe;

import net.minecraft.item.ItemStack;

public class CeramicFormerRecipe {

    private final ItemStack input;
    private final ItemStack output;
    private final int work;

    public CeramicFormerRecipe(ItemStack input, ItemStack output, int work) {
        this.input = input;
        this.output = output;
        this.work = work;
    }

    public boolean isItemValid(ItemStack itemStack) {
        return itemStack != null && input.getItem().equals(itemStack.getItem()) && input.getItemDamage() == itemStack.getItemDamage() && input.stackSize <= itemStack.stackSize;
    }

    public boolean isItemValidIgnoreAmount(ItemStack itemStack) {
        return itemStack != null && input.getItem().equals(itemStack.getItem()) && input.getItemDamage() == itemStack.getItemDamage();
    }

    public ItemStack getResults() {
        return output.copy();
    }

    public int getWork() {
        return work;
    }

    public ItemStack getItem() {
        return input.copy();
    }
}
