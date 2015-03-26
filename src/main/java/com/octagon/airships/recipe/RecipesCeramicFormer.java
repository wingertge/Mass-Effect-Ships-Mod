package com.octagon.airships.recipe;

import com.octagon.airships.init.ModItems;
import cpw.mods.fml.common.Mod;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RecipesCeramicFormer {

    private static List<CeramicFormerRecipe> recipes;

    static {
        recipes = new ArrayList<CeramicFormerRecipe>();

        recipes.add(new CeramicFormerRecipe(new ItemStack(ModItems.superConductingAlloy, 1), new ItemStack(Blocks.dirt, 1), 200));
    }

    public static boolean isItemValid(ItemStack item) {
        for(CeramicFormerRecipe recipe : recipes)
            if(recipe.isItemValidIgnoreAmount(item)) return true;
        return false;
    }

    public static boolean isValid(ItemStack cfrcPlates, ItemStack material) {
        for(CeramicFormerRecipe recipe : recipes)
            if(recipe.isItemValid(material) && cfrcPlates != null && cfrcPlates.stackSize > 0) return true;
        return false;
    }

    private static CeramicFormerRecipe getValidRecipe(ItemStack material) {
        for(CeramicFormerRecipe recipe : recipes)
            if(recipe.isItemValid(material)) return recipe;
        return null;
    }

    public static ItemStack[] doCraft(ItemStack[] input) {
        ItemStack cfrcPlate = input[0];
        ItemStack material = input[1];

        if(!isValid(cfrcPlate, material)) return input;

        CeramicFormerRecipe recipe = getValidRecipe(material);

        if(cfrcPlate.stackSize == 1)
            cfrcPlate = null;
        else cfrcPlate.stackSize -= 1;

        if(material.stackSize == recipe.getItem().stackSize)
            material = null;
        else material.stackSize -= recipe.getItem().stackSize;

        ItemStack[] results = new ItemStack[3];

        results[0] = cfrcPlate;
        results[1] = material;
        results[2] = recipe.getResults();
        return results;
    }

    public static int getWorkForItem(ItemStack item) {
        if(!isValid(new ItemStack(ModItems.cfrcPlate), item)) return 0;
        return getValidRecipe(item).getWork();
    }
}
