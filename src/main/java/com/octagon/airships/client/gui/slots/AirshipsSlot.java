package com.octagon.airships.client.gui.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class AirshipsSlot extends Slot {

    protected boolean active;
    public int activeSlotNumber;

    public AirshipsSlot(IInventory iinventory, int par2, int par3, int par4, boolean flag)
    {
        super(iinventory, par2, par3, par4);
        active = flag;
    }

    public AirshipsSlot(IInventory inventory, int id, int x, int y)
    {
        this(inventory, id, x, y, true);
    }

    public void setActive (boolean flag)
    {
        active = flag;
    }

    public boolean getActive ()
    {
        return active;
    }
}
