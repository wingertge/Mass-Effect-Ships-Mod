package com.octagon.airships.block;

import com.octagon.airships.block.multiblock.BlockMultiblockPart;
import com.octagon.airships.reference.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLaunchPadIO extends BlockMultiblockPart {
    private static final class Icons {
        public static IIcon endsIcon;
    }

    public BlockLaunchPadIO() {
        super(Material.iron);
    }

    @Override
    public void registerBlockIcons(IIconRegister registry) {
        super.registerBlockIcons(registry);

        Icons.endsIcon = registry.registerIcon(Reference.MOD_ID + ":launchPadIO_ends");
        setTexture(ForgeDirection.UP, Icons.endsIcon);
        setTexture(ForgeDirection.DOWN, Icons.endsIcon);
    }
}
