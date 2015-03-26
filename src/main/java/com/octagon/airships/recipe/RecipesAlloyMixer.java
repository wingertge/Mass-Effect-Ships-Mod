package com.octagon.airships.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.*;

public class RecipesAlloyMixer {
    private static List<AlloyMixerRecipe> recipes;

    static {
        recipes = new ArrayList<AlloyMixerRecipe>();
        recipes.add(new AlloyMixerRecipe(
                new ItemStack(Items.iron_ingot),
                new ItemStack(Items.carrot),
                new ItemStack(Items.potato, 2),
                new ItemStack(Blocks.dirt, 2)));
    }

    public static boolean isValidRecipe(ItemStack[] items) {
        if(items.length != 3) return false;
        HashMap<Integer, Integer> itemPositions = new HashMap<Integer, Integer>();

        for(AlloyMixerRecipe recipe : recipes) {
            for(int i = 0; i < 3; i++) {
                if(items[i] == null) return false;

                itemPositions.put(i, recipe.getItemPosition(items[i]));
            }

            Collection<Integer> valuesList = itemPositions.values();
            Set<Integer> valuesSet = new HashSet<Integer>(itemPositions.values());
            if(valuesList.size() == valuesSet.size() && !valuesList.contains(-1)) return true;
        }

        return false;
    }

    public static boolean isValidRecipePartial(ItemStack[] items) {
        HashMap<Integer, Integer> itemPositions = new HashMap<Integer, Integer>();

        for(AlloyMixerRecipe recipe : recipes) {
            for(int i = 0; i < 3; i++) {
                if(items[i] == null) {
                    itemPositions.put(i, Integer.MAX_VALUE - i);
                    continue;
                }

                itemPositions.put(i, recipe.getItemPositionIgnoreAmount(items[i]));
            }

            Collection<Integer> valuesList = itemPositions.values();
            Set<Integer> valuesSet = new HashSet<Integer>(itemPositions.values());
            if(valuesList.size() == valuesSet.size() && !valuesList.contains(-1)) return true;
        }

        return false;
    }

    public static ItemStack[] doCraft(ItemStack[] input) {
        if(!isValidRecipe(input)) return input;

        AlloyMixerRecipe recipe = findValidRecipe(input);
        ItemStack[] output = new ItemStack[input.length + 1];

        for(int i = 0; i < input.length; i++) {
            int position = recipe.getItemPosition(input[i]);
            if(input[i].stackSize == recipe.getItemAt(position).stackSize) output[i] = null;
            else  {
                ItemStack newInput = input[i];
                newInput.stackSize -= recipe.getItemAt(position).stackSize;
                output[i] = newInput;
            }
        }

        output[input.length] = recipe.getResults();

        return output.clone();
    }

    private static AlloyMixerRecipe findValidRecipe(ItemStack[] items) {
        HashMap<Integer, Integer> itemPositions = new HashMap<Integer, Integer>();

        for(AlloyMixerRecipe recipe : recipes) {
            for(int i = 0; i < 3; i++) {
                itemPositions.put(i, recipe.getItemPosition(items[i]));
            }

            Collection<Integer> valuesList = itemPositions.values();
            Set<Integer> valuesSet = new HashSet<Integer>(itemPositions.values());
            if(valuesList.size() == valuesSet.size() && !valuesList.contains(-1)) return recipe;
        }

        return null;
    }
}
