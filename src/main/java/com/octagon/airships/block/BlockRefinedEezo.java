package com.octagon.airships.block;

import com.octagon.airships.MassEffectShips;
import com.octagon.airships.block.multiblock.BlockMultiblockPart;
import com.octagon.airships.reference.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockRefinedEezo extends BlockMultiblockPart {
    private static final class Icons {
        public static IIcon blockTexture;
        public static IIcon blank;
    }

    public BlockRefinedEezo() {
        super(Material.rock);
    }

    @Override
    protected Object getModInstance() {
        return MassEffectShips.instance;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void registerBlockIcons(IIconRegister registry) {
        super.registerBlockIcons(registry);

        Icons.blockTexture = registry.registerIcon(Reference.MOD_ID + ":refinedEezoBlock");
        Icons.blank = registry.registerIcon(Reference.MOD_ID + ":blank");

        setDefaultTexture(Icons.blockTexture);
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        int meta = world.getBlockMetadata(x, y, z);

        return meta == 0 ? Icons.blockTexture : Icons.blank;
    }
}
