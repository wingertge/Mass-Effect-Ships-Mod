package com.octagon.airships.block;

import com.octagon.airships.MassEffectShips;
import com.octagon.airships.block.multiblock.BlockMultiblockPart;
import com.octagon.airships.reference.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockEezoCoreHull extends BlockMultiblockPart {
    private static final class Icons {
        public static IIcon blockTexture;
        public static IIcon blank;
    }

    public BlockEezoCoreHull() {
        super(Material.iron);
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

        Icons.blockTexture = registry.registerIcon(Reference.MOD_ID + ":eezoCoreHull");
        Icons.blank = registry.registerIcon(Reference.MOD_ID + ":blank");
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        int meta = world.getBlockMetadata(x, y, z);

        return meta == 0 ? Icons.blockTexture : Icons.blank;
    }
}
