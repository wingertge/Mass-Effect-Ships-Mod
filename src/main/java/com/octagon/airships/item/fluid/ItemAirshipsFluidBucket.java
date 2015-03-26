package com.octagon.airships.item.fluid;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ItemAirshipsFluidBucket extends ItemFluidBucket {
    public ItemAirshipsFluidBucket() {
        super(1000);
    }

    protected ItemAirshipsFluidBucket(int capacity) {
        super(capacity);
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        if(resource.amount == 1000)
            return super.fill(container, resource, doFill);
        else return 0;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        if(maxDrain == 1000)
            return super.drain(container, maxDrain, doDrain);
        else return null;
    }
}
