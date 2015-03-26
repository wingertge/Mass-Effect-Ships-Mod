package com.octagon.airships.item.fluid;

import com.octagon.airships.init.ModFluids;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;

import java.util.List;

public class ItemPressureVessel extends ItemFluidContainer {

    public ItemPressureVessel() {
        super(8000);
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        if(isCompatibleLiquid(resource))
            return super.fill(container, resource, doFill);
        else return 0;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        return super.drain(container, maxDrain, doDrain);
    }

    private static boolean isCompatibleLiquid(FluidStack fluid) {
        return fluid.getFluid().equals(ModFluids.liquidDeuterium);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean extended) {
        if(getFluid(stack) != null)
            lines.add(StatCollector.translateToLocalFormatted("masseffectships.misc.fluidStorage", getFluid(stack).getLocalizedName(), getFluid(stack).amount, getCapacity(stack)));
        else lines.add(StatCollector.translateToLocal("masseffectships.misc.empty"));
    }
}
