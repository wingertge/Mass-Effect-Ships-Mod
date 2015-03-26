package com.octagon.airships.block;

import com.octagon.airships.MassEffectShips;
import com.octagon.airships.block.tileentity.TileEntityAlloyMixer;
import com.octagon.airships.reference.GUIs;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAlloyMixer extends BlockMachineBase {
    public BlockAlloyMixer() {
        super("Alloy Mixer", Material.iron, "alloyMixer", "alloyMixer_on", "machine", "machine"); //TODO: Localize name
        setBlockName("alloyMixer");
    }

    /*@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float what, float these, float are) {
        if (!player.isSneaking())
        {
            TileEntity tileentity = world.getTileEntity(x, y, z);
            if (tileentity != null)
            {
                player.openGui(MassEffectShips.instance, GUIs.MACHINE_ALLOY_MIXER.ordinal(), world, x, y, z);
                return true;
            }
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityAlloyMixer();
    } */
}
