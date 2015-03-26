package com.octagon.airships.init;

import net.minecraft.init.Items;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.List;

public class ModRecipes {
    public static void init() {
        @SuppressWarnings("unchecked")
        final List<IRecipe> recipeList = CraftingManager.getInstance().getRecipeList();

        if(ModItems.probe != null) {
            if(ModItems.probeMining != null) {
                recipeList.add(new ShapelessOreRecipe(ModItems.probeMining, ModItems.probe, Items.diamond_pickaxe));
                recipeList.add(new ShapelessOreRecipe(Items.diamond_pickaxe, ModItems.probeMining.setContainerItem(ModItems.probe)));
            }
        }
    }
}
