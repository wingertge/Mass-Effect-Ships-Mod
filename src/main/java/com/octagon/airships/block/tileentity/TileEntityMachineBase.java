package com.octagon.airships.block.tileentity;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyReceiver;
import com.octagon.airships.block.item.ItemMachine;
import com.octagon.airships.client.gui.machine.ContainerMachineBase;
import com.octagon.airships.client.gui.machine.GuiOpenMachineBase;
import com.octagon.airships.reference.Config;
import com.octagon.airships.sync.SyncableEnergyStorage;
import com.octagon.airships.sync.SyncableItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.api.IActivateAwareTile;
import openmods.api.IHasGui;
import openmods.gui.listener.IValueChangedListener;
import openmods.sync.SyncableBoolean;
import openmods.sync.drops.DroppableTileEntity;
import openmods.sync.drops.StoreOnDrop;

public class TileEntityMachineBase extends DroppableTileEntity implements ISidedInventory, IEnergyReceiver, IHasGui, IActivateAwareTile {

    private SyncableBoolean active;

    @StoreOnDrop(name = ItemMachine.ENERGY_STORAGE_TAG)
    protected SyncableEnergyStorage energyStorage;
    private SyncableItemStack battery;
    private String name;

    public TileEntityMachineBase() {
        this("Machine");
    }

    @Override
    protected void createSyncedFields() {
        active = new SyncableBoolean(false);
        energyStorage = new SyncableEnergyStorage(Config.EnergyStorage.MACHINE_GENERIC);
        battery = new SyncableItemStack();
    }

    public TileEntityMachineBase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active.get();
    }
    public void setActive(boolean active) {
        this.active.set(active);
    }

    @Override
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
    }

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
        ForgeDirection facing;
        switch (blockMetadata) {
            case 1: facing = ForgeDirection.WEST;
                break;
            case 2: facing = ForgeDirection.NORTH;
                break;
            case 3: facing = ForgeDirection.EAST;
                break;
            default: facing = ForgeDirection.SOUTH;
                break;
        }
        return facing != direction;
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

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot == 0 ? getBattery() : null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if(slot != 0) return null;
        if(amount >= getBattery().stackSize) {
            ItemStack bat = getBattery();
            setBattery(null);
            this.markDirty();
            return bat;
        } else {
            ItemStack bat = getBattery();
            bat.stackSize -= amount;
            setBattery(bat);
            return new ItemStack(getBattery().getItem(), amount, getBattery().getItemDamage());
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if(slot == 0) {
            ItemStack battery = getBattery();
            setBattery(null);
            return battery;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }

        setBattery(itemStack);

        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return "Battery";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if(side != 0) return new int[0];
        int[] slots = new int[1];
        slots[0] = 0;
        return slots;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return slot == 0 && side == 0 && (item.getItem() instanceof IEnergyContainerItem);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return slot == 0 && side == 0;
    }

    protected SyncableEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public ItemStack getBattery() {
        return battery.get() != null ? battery.get().copy() : null;
    }

    public void setBattery(ItemStack battery) {
        this.battery.set(battery);
    }

    public <T> void subscribeListener(String name, IValueChangedListener<T> listener) {
        if(name.equalsIgnoreCase("energyStored") || name.equalsIgnoreCase("maxEnergyStored") || name.equalsIgnoreCase("maxReceive") || name.equalsIgnoreCase("maxExtract")) {
            energyStorage.subscribeListener(name, listener);
        }
    }

    @Override
    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        player.addChatMessage(new ChatComponentText("Meta: " + blockMetadata));
        return true;
    }

    @Override
    public Object getServerGui(EntityPlayer player) {
        return new ContainerMachineBase<>(player.inventory, this);
    }

    @Override
    public Object getClientGui(EntityPlayer player) {
        return new GuiOpenMachineBase<>(new ContainerMachineBase<>(player.inventory, this));
    }

    @Override
    public boolean canOpenGui(EntityPlayer player) {
        return true;
    }
}
