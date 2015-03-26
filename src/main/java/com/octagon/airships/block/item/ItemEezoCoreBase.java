package com.octagon.airships.block.item;

import net.minecraft.block.Block;
import openmods.item.ItemOpenBlock;

public class ItemEezoCoreBase extends ItemOpenBlock {
    public static final String TAG_RADIUS = "Radius";
    public static final String TAG_COLOR = "Color";
    public static final String TAG_GUIDE_ACTIVE = "Guide Active";

    public ItemEezoCoreBase(Block block) {
        super(block);
    }
}
