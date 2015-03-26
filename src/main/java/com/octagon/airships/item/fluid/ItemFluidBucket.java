package com.octagon.airships.item.fluid;

import com.octagon.airships.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;

import java.util.List;

public abstract class ItemFluidBucket extends ItemFluidContainer {
    public ItemFluidBucket(int capacity) {
        super(capacity);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float px, float py, float pz) {
        Block b = world.getBlock(x, y, z);
        if(b == null) return true;

        if(b instanceof BlockFluidBase) {
            BlockFluidBase fluidBlock = (BlockFluidBase)b;
            if(fill(stack, new FluidStack(fluidBlock.getFluid(), 1000), false) == 1000)
                fill(stack, new FluidStack(fluidBlock.getFluid(), 1000), true);
        } else {
            FluidStack oldFluidStack = getFluid(stack);
            FluidStack fluidStack = drain(stack, 1000, false);
            if(oldFluidStack != null && fluidStack != null && fluidStack.amount == oldFluidStack.amount - 1000) {
                int newX = x;
                int newY = y;
                int newZ = z;
                switch (side) {
                    case 0: newY -= 1;
                        break;
                    case 1: newY += 1;
                        break;
                    case 2: newZ -= 1;
                        break;
                    case 3: newZ += 1;
                        break;
                    case 4: newX -= 1;
                        break;
                    case 5: newX += 1;
                        break;
                }

                if(!world.getBlock(newX, newY, newZ).equals(Blocks.air))
                    LogHelper.error(String.format("Block replaced by %s not air (actually %s)!", b.getUnlocalizedName(), world.getBlock(newX, newY, newZ).getUnlocalizedName()));

                world.setBlock(newX, newY, newZ, fluidStack.getFluid().getBlock());
                drain(stack, 1000, true);
            }
        }
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean extended) {
        if(getFluid(stack) == null) return;
        lines.add(StatCollector.translateToLocalFormatted("masseffectships.misc.fluidStorage", getFluid(stack).getLocalizedName(), getFluid(stack).amount, getCapacity(stack)));
    }
}
