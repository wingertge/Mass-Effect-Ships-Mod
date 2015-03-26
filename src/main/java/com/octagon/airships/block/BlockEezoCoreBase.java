package com.octagon.airships.block;

import com.octagon.airships.block.multiblock.BlockMultiblockBase;
import com.octagon.airships.block.tileentity.TileEntityMultiblockPart;
import com.octagon.airships.reference.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEezoCoreBase extends BlockMultiblockBase {
    private static int x;
    private static int y;
    private static int z;

    public static class Icons {
        public static IIcon marker;
        public static IIcon hull;
        public static IIcon blank;
    }

    public BlockEezoCoreBase() {
        super(Material.iron);
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        super.registerBlockIcons(iconRegister);
        Icons.marker = iconRegister.registerIcon(Reference.MOD_ID + ":guide");
        Icons.hull = iconRegister.registerIcon(Reference.MOD_ID + ":eezoCoreHull");
        Icons.blank = iconRegister.registerIcon(Reference.MOD_ID + ":blank");

        setDefaultTexture(Icons.hull);
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        BlockEezoCoreBase.x = x;
        BlockEezoCoreBase.y = y;
        BlockEezoCoreBase.z = z;

        return meta;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if(tileEntity != null && tileEntity instanceof TileEntityMultiblockPart) {
            TileEntityMultiblockPart part = (TileEntityMultiblockPart)tileEntity;
            if(part.getStructure() != null && part.getStructure().isComplete()) return Icons.blank;
        }
        return Icons.hull;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
