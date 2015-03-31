package com.octagon.airships.client.gui.machine;

import com.octagon.airships.block.tileentity.TileEntityElectrolyzer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerElectrolyzer extends ContainerMachineBase<TileEntityElectrolyzer> {
    public ContainerElectrolyzer(InventoryPlayer inventory, TileEntityElectrolyzer machine) {
        super(inventory, machine);
    }
}
