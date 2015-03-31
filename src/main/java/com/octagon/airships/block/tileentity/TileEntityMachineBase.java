package com.octagon.airships.block.tileentity;

import cofh.api.energy.IEnergyReceiver;
import com.octagon.airships.block.item.ItemMachine;
import com.octagon.airships.reference.Config;
import com.octagon.airships.sync.SyncableEnergyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.api.IActivateAwareTile;
import openmods.gui.listener.IValueChangedListener;
import openmods.inventory.GenericInventory;
import openmods.inventory.IInventoryProvider;
import openmods.inventory.TileEntityInventory;
import openmods.sync.SyncableBoolean;
import openmods.sync.drops.DroppableTileEntity;
import openmods.sync.drops.StoreOnDrop;

public class TileEntityMachineBase extends DroppableTileEntity implements IEnergyReceiver, IActivateAwareTile, IInventoryProvider {

    private SyncableBoolean active;

    @StoreOnDrop(name = ItemMachine.ENERGY_STORAGE_TAG)
    protected SyncableEnergyStorage energyStorage;
    protected GenericInventory inventory;

    public TileEntityMachineBase() {

    }

    @Override
    protected void createSyncedFields() {
        active = new SyncableBoolean(false);
        energyStorage = new SyncableEnergyStorage(Config.EnergyStorage.MACHINE_GENERIC);
        inventory = registerInventoryCallback(new TileEntityInventory(this, "machine", false, 1));
    }

    public boolean isActive() {
        return active.get();
    }
    public void setActive(boolean active) {
        this.active.set(active);
    }

    /*@Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound compound = new NBTTagCompound();
        writeToNBT(compound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        readFromNBT(packet.func_148857_g());
    }*/

    @Override
    public int receiveEnergy(ForgeDirection from, int energy, boolean simulate) {
        return getEnergyStorage().receiveEnergy(energy, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return getEnergyStorage().getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return getEnergyStorage().getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection direction) {
        return direction != getRotation().getOpposite();
    }

    public int getEnergyStoredScaled(int max) {
        double percent = (double)getEnergyStorage().getEnergyStored() / (double)getEnergyStorage().getMaxEnergyStored();
        return (int)(percent * max);
    }

    public void setEnergyStored(int value) {
        getEnergyStorage().setEnergyStored(value);
    }

    public void setMaxEnergyStored(int value) {
        getEnergyStorage().setMaxEnergyStored(value);
    }


    protected SyncableEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public ItemStack getBattery() {
        return getInventory().getStackInSlot(0);
    }

    public void setBattery(ItemStack battery) {
        getInventory().setInventorySlotContents(0, battery);
    }

    public <T> void subscribeListener(String name, IValueChangedListener<T> listener) {
        if(name.equalsIgnoreCase("energyStored") || name.equalsIgnoreCase("maxEnergyStored") || name.equalsIgnoreCase("maxReceive") || name.equalsIgnoreCase("maxExtract")) {
            getEnergyStorage().subscribe(name, listener);
        }
    }

    @Override
    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        player.addChatMessage(new ChatComponentText("Meta: " + blockMetadata));
        return true;
    }

    @Override
    public IInventory getInventory() {
        return inventory;
    }
}
