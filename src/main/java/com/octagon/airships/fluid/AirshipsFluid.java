package com.octagon.airships.fluid;

import com.octagon.airships.item.fluid.ItemFluidBucket;
import net.minecraftforge.fluids.Fluid;

public class AirshipsFluid extends Fluid {
    private ItemFluidBucket bucket = null;

    public AirshipsFluid(String fluidName) {
        super(fluidName);
    }

    public ItemFluidBucket getBucket() {
        return bucket;
    }

    public AirshipsFluid setBucket(ItemFluidBucket bucket) {
        this.bucket = bucket;
        return this;
    }
}
