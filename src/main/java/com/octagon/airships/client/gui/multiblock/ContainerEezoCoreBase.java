package com.octagon.airships.client.gui.multiblock;


import com.octagon.airships.block.tileentity.TileEntityEezoCoreMultiblock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import openmods.container.ContainerBase;

public class ContainerEezoCoreBase extends ContainerBase<TileEntityEezoCoreMultiblock> {
    public ContainerEezoCoreBase(IInventory playerInventory, TileEntityEezoCoreMultiblock owner) {
        super(playerInventory, new InventoryBasic("asd", false, 0), owner);
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
}
