package com.octagon.airships.client.gui.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class SlotLiquidContainer extends AirshipsSlot {
    public SlotLiquidContainer(IInventory iinventory, int par2, int par3, int par4) {
        super(iinventory, par2, par3, par4);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof IFluidContainerItem;
    }
}
