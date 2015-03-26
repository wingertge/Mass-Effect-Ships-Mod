package com.octagon.airships.block.multiblock;

import com.octagon.airships.MassEffectShips;
import com.octagon.airships.block.tileentity.TileEntityMultiblockPart;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.block.OpenBlock;

public abstract class BlockMultiblockPart extends OpenBlock implements ITileEntityProvider {
   protected BlockMultiblockPart(Material material) {
        super(material);
    }

   @Override
   protected Object getModInstance() {
        return MassEffectShips.instance;
    }

   @Override
   public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
       return new TileEntityMultiblockPart();
   }

    @Override
    public void afterBlockPlaced(World world, EntityPlayer player, ItemStack stack, int x, int y, int z, ForgeDirection side, ForgeDirection blockDir, float hitX, float hitY, float hitZ, int itemMeta) {
        for(int i = 0; i < 6; i++) {
            int newX = x;
            int newY = y;
            int newZ = z;

            switch (i) {
                case 0: newY -= 1; break;
                case 1: newY += 1; break;
                case 2: newZ -= 1; break;
                case 3: newZ += 1; break;
                case 4: newX -= 1; break;
                case 5: newX += 1; break;
            }

            TileEntity tileEntity = world.getTileEntity(newX, newY, newZ);
            if(tileEntity != null) {
                if(tileEntity instanceof TileEntityMultiblock) {
                    ((TileEntityMultiblock) tileEntity).queueStructureValidation();
                    break;
                }
                if(tileEntity instanceof TileEntityMultiblockPart) {
                    if(((TileEntityMultiblockPart) tileEntity).onNeighbourPlaced(this)) {
                        break;
                    }
                }
            }
        }

        super.afterBlockPlaced(world, player, stack, x, y, z, side, blockDir, hitX, hitY, hitZ, itemMeta);
    }
}
