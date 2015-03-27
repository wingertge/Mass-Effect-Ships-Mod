package com.octagon.airships.client.gui.multiblock;

import com.octagon.airships.block.tileentity.TileEntityLaunchPadMultiblock;
import com.octagon.airships.client.gui.slots.SlotBattery;
import com.octagon.airships.client.gui.slots.SlotLiquidContainer;
import com.octagon.airships.client.gui.slots.SlotOutput;
import com.octagon.airships.client.gui.slots.SlotProbe;
import net.minecraft.inventory.IInventory;
import openmods.container.ContainerInventoryProvider;

public class ContainerLaunchPadMultiblock extends ContainerInventoryProvider<TileEntityLaunchPadMultiblock> {

    public ContainerLaunchPadMultiblock(IInventory playerInventory, TileEntityLaunchPadMultiblock owner) {
        super(playerInventory, owner);

        addSlotToContainer(new SlotBattery(owner.getInventory(), 0, 7, 61));
        addSlotToContainer(new SlotLiquidContainer(owner.getInventory(), 1, 27, 61));

        for(int i = 2; i < 6; i++) {
            addSlotToContainer(new SlotOutput(owner.getInventory(), i, 52, 7 + (i - 2) * 18));
        }

        int totalSlots = owner.getInventory().getSizeInventory() - 6;

        if(totalSlots > 20) totalSlots = 20;
        for(int i = 0; i < totalSlots; i++) {
            addSlotToContainer(new SlotProbe(owner.getInventory(), i + 6, 74 + (i % 5) * 18, 7 + (i / 5 * 18)));
        }

        addPlayerInventorySlots(84);
    }
}
