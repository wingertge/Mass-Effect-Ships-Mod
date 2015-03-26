package com.octagon.airships.block.tileentity;

import com.octagon.airships.MassEffectShips;
import com.octagon.airships.block.item.ItemMachine;
import com.octagon.airships.client.gui.machine.ContainerCeramicFormer;
import com.octagon.airships.client.gui.machine.GuiOpenCeramicFormer;
import com.octagon.airships.recipe.RecipesCeramicFormer;
import com.octagon.airships.reference.Config;
import com.octagon.airships.reference.GUIs;
import com.octagon.airships.sync.IMonitoredValue;
import com.octagon.airships.sync.MonitoredInt;
import com.octagon.airships.sync.SyncableEnergyStorage;
import com.octagon.airships.sync.SyncableItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import openmods.gui.listener.IValueChangedListener;
import openmods.sync.SyncableBoolean;
import openmods.sync.SyncableInt;
import openmods.sync.drops.StoreOnDrop;

import java.lang.reflect.Field;
import java.util.Arrays;

public class TileEntityCeramicFormer extends TileEntityMachineBase {

    @StoreOnDrop(name = ItemMachine.ENERGY_STORAGE_TAG)
    private SyncableEnergyStorage energyStorage;
    private SyncableBoolean active;

    private MonitoredInt currentWork;
    private MonitoredInt maxWork;

    private SyncableItemStack battery;
    private SyncableItemStack input1;
    private SyncableItemStack input2;
    private SyncableItemStack output;
    private boolean lastActive = false;

    public TileEntityCeramicFormer() {
        super();
    }

    @Override
    protected void createSyncedFields() {
        energyStorage = new SyncableEnergyStorage(Config.EnergyStorage.CERAMIC_FORMER);
        active = new SyncableBoolean(false);

        currentWork = new MonitoredInt(0);
        maxWork = new MonitoredInt(200);

        battery = new SyncableItemStack();
        input1 = new SyncableItemStack();
        input2 = new SyncableItemStack();
        output = new SyncableItemStack();
    }

    public int getWorkProgressScaled(int max) {
        return (int)(((double)getCurrentWork() / (double)getMaxWork()) * max);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        setActive(false);

        if(input1.get() != null && input2.get() != null) {
            if (RecipesCeramicFormer.isValid(input1.get().copy(), input2.get().copy())) {
                setMaxWork(RecipesCeramicFormer.getWorkForItem(input2.get().copy()));

                if (getEnergyStorage().getEnergyStored() >= Config.EnergyUsage.CERAMIC_FORMER) {
                    if (getCurrentWork() < getMaxWork()) {
                        inc(currentWork);
                        getEnergyStorage().extractEnergy(Config.EnergyUsage.CERAMIC_FORMER, false);
                        setActive(true);
                    }

                    ItemStack[] inputSlots = new ItemStack[2];
                    inputSlots[0] = input1.get().copy();
                    inputSlots[1] = input2.get().copy();

                    if (getCurrentWork() >= getMaxWork()) {
                        ItemStack[] craftResult = RecipesCeramicFormer.doCraft(inputSlots);
                        if (craftResult.length != 3) return;
                        if (output.get() == null || output.get().stackSize == 0) {
                            input1.set(craftResult[0]);
                            input2.set(craftResult[1]);
                            output.set(craftResult[2]);

                            setCurrentWork(0);
                        } else if (craftResult[2].getItem().equals(output.get().getItem())
                                && craftResult[2].getItemDamage() == output.get().getItemDamage()
                                && craftResult[2].stackSize + output.get().stackSize <= output.get().getMaxStackSize()) {

                            input1.set(craftResult[0]);
                            input2.set(craftResult[1]);
                            output.increaseStackSize(craftResult[2].stackSize);

                            setCurrentWork(0);
                        }
                    }
                }
            }
        }

        lastActive = isActive();
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        if(slot == 0) {
            super.setInventorySlotContents(slot, itemStack);
            return;
        }
        if(slot > 3) return;

        if(itemStack != null && itemStack.stackSize > getInventoryStackLimit())
            itemStack.stackSize = getInventoryStackLimit();

        getSlot(slot).set(itemStack);
    }

    @Override
    public int getSizeInventory() {
        return 3 + super.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot == 0 ? super.getStackInSlot(slot) : getSlot(slot).get();
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if(slot == 0) return super.getStackInSlotOnClosing(slot);

        ItemStack itemStack = getSlot(slot).get() != null ? getSlot(slot).get().copy() : null;
        getSlot(slot).set(null);
        return itemStack;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if(slot == 0) return super.decrStackSize(slot, amount);
        if(slot > 3) return null;
        if(getSlot(slot).get() == null) return null;

        if(getSlot(slot).get().stackSize <= amount) {
            ItemStack item = getSlot(slot).get().copy();
            getSlot(slot).set(null);
            return item;
        } else {
            getSlot(slot).decreaseStackSize(amount);
            return new ItemStack(getSlot(slot).get().getItem(), amount, getSlot(slot).get().getItemDamage());
        }
    }

    public void setMaxWork(int maxWork) {
        this.maxWork.set(maxWork);
    }

    public void setCurrentWork(int currentWork) {
        this.currentWork.set(currentWork);
    }

    public int getCurrentWork() {
        return currentWork.get();
    }

    public int getMaxWork() {
        return maxWork.get();
    }

    @Override
    public boolean isActive() {
        return active.get();
    }

    @Override
    public void setActive(boolean active) {
        if(lastActive != active) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        this.active.set(active);
    }

    @Override
    protected SyncableEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    private void inc(MonitoredInt i) {
        i.set(i.get() + 1);
    }

    private void dec(SyncableInt i) {
        i.set(i.get() - 1);
    }

    @Override
    public ItemStack getBattery() {
        return battery.get() != null ? battery.get().copy() : null;
    }

    @Override
    public void setBattery(ItemStack battery) {
        this.battery.set(battery);
    }

    private SyncableItemStack getSlot(int slot) {
        switch (slot) {
            case 0:
                return battery;
            case 1:
                return input1;
            case 2:
                return input2;
            case 3:
                return output;
            default:
                return battery;
        }
    }

    @Override
    public boolean canOpenGui(EntityPlayer player) {
        return true;
    }

    @Override
    public Object getClientGui(EntityPlayer player) {
        return new GuiOpenCeramicFormer(new ContainerCeramicFormer(player.inventory, this));
    }

    @Override
    public Object getServerGui(EntityPlayer player) {
        return new ContainerCeramicFormer(player.inventory, this);
    }

    @Override
    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(!player.isSneaking()) player.openGui(MassEffectShips.instance, GUIs.DYNAMIC.ordinal(), worldObj, xCoord, yCoord, zCoord);
        return true;
    }

    public <T> void subscribeListener(String name, IValueChangedListener<T> listener) {
        if(name.equalsIgnoreCase("energyStored") || name.equalsIgnoreCase("maxEnergyStored") || name.equalsIgnoreCase("maxReceive") || name.equalsIgnoreCase("maxExtract")) {
            energyStorage.subscribeListener(name, listener);
        } else {
            for(Field field : getClass().getDeclaredFields()) {
                boolean implementsInterface = Arrays.asList(field.getType().getInterfaces()).contains(IMonitoredValue.class);
                if(field.getName().equalsIgnoreCase(name) && implementsInterface)
                    try {
                        ((IMonitoredValue)field.get(this)).subscribe(listener);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}
