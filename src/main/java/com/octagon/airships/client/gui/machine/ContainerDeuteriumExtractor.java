package com.octagon.airships.client.gui.machine;

import com.octagon.airships.block.tileentity.TileEntityDeuteriumExtractor;
import com.octagon.airships.client.gui.slots.SlotLiquidContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerDeuteriumExtractor extends ContainerMachineBase<TileEntityDeuteriumExtractor> {
    public ContainerDeuteriumExtractor(InventoryPlayer inventory, TileEntityDeuteriumExtractor machine) {
        super(inventory, machine);

        addSlotToContainer(new SlotLiquidContainer(machine.getInventory(), 1, 62, 53));
        addSlotToContainer(new SlotLiquidContainer(machine.getInventory(), 2, 116, 53));
    }
}
