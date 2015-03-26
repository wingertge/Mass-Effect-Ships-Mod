package com.octagon.airships.block;

import com.octagon.airships.block.multiblock.BlockMultiblockPart;
import com.octagon.airships.reference.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLaunchPad extends BlockMultiblockPart {
    public static final class Icons {
        public static IIcon top;
        public static IIcon sides;
    }

    public BlockLaunchPad() {
        super(Material.iron);
    }

    @Override
    public void registerBlockIcons(IIconRegister registry) {
        super.registerBlockIcons(registry);

        Icons.top = registry.registerIcon(Reference.MOD_ID + ":launchPad_top");
        Icons.sides = registry.registerIcon(Reference.MOD_ID + ":launchPad_side");

        setTexture(ForgeDirection.UP, Icons.top);
        setTexture(ForgeDirection.DOWN, Icons.sides);
        setTexture(ForgeDirection.NORTH, Icons.sides);
        setTexture(ForgeDirection.SOUTH, Icons.sides);
        setTexture(ForgeDirection.EAST, Icons.sides);
        setTexture(ForgeDirection.WEST, Icons.sides);
    }
}
