package com.octagon.airships.client.gui.multiblock;

import com.octagon.airships.block.tileentity.TileEntityLaunchPadMultiblock;
import com.octagon.airships.client.gui.slots.SlotBattery;
import com.octagon.airships.client.gui.slots.SlotLiquidContainer;
import com.octagon.airships.client.gui.slots.SlotOutput;
import com.octagon.airships.client.gui.slots.SlotProbe;
import net.minecraft.inventory.IInventory;
import openmods.container.ContainerBase;

public class ContainerLaunchPadMultiblock extends ContainerBase<TileEntityLaunchPadMultiblock> {

    public ContainerLaunchPadMultiblock(IInventory playerInventory, TileEntityLaunchPadMultiblock owner) {
        super(playerInventory, owner.getGhostInventory(), owner);

        addSlotToContainer(new SlotBattery(owner.getGhostInventory(), 0, 7, 61));
        addSlotToContainer(new SlotLiquidContainer(owner.getGhostInventory(), 1, 27, 61));

        addSlotToContainer(new SlotOutput(owner.getGhostInventory(), 2, 47, 41));
        addSlotToContainer(new SlotOutput(owner.getGhostInventory(), 2, 47, 61));

        int totalSlots = owner.getGhostInventory().getSizeInventory() - 4;

        if(totalSlots > 20) totalSlots = 20;
        for(int i = 0; i < totalSlots; i++) {
            addSlotToContainer(new SlotProbe(owner.getGhostInventory(), i + 4, 74 + (i % 5) * 18, 7 + (i / 5 * 18)));
        }

        addPlayerInventorySlots(84);
    }
}
