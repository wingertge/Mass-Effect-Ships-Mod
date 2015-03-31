package com.octagon.airships.block;

import com.octagon.airships.reference.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;

public abstract class BlockAirshipsFluid extends BlockFluidBase {
    protected BlockAirshipsFluid(Fluid fluid, Material material) {
        super(fluid, material);
    }

    public static final class Icons {
        public static IIcon transparent;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return Icons.transparent;
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        Icons.transparent = register.registerIcon(Reference.MOD_ID + ":transparentLiquid");

        getFluid().setIcons(Icons.transparent);
    }
}
