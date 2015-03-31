package com.octagon.airships.block.tileentity;

import com.octagon.airships.block.item.ItemMachine;
import com.octagon.airships.client.gui.machine.ContainerAlloyMixer;
import com.octagon.airships.client.gui.machine.GuiAlloyMixer;
import com.octagon.airships.recipe.RecipesAlloyMixer;
import com.octagon.airships.reference.Config;
import com.octagon.airships.sync.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import openmods.api.IHasGui;
import openmods.gui.listener.IValueChangedListener;
import openmods.sync.drops.StoreOnDrop;

import java.lang.reflect.Field;
import java.util.Arrays;

public class TileEntityAlloyMixer extends TileEntityMachineBase implements IHasGui {
    //private ItemStack[] contents = new ItemStack[4];
    private SyncableItemStack battery;

    private SyncableItemStack input1;
    private SyncableItemStack input2;
    private SyncableItemStack input3;
    private SyncableItemStack output;

    @StoreOnDrop(name = ItemMachine.ENERGY_STORAGE_TAG)
    private SyncableEnergyStorage energyStorage;
    private MonitoredBoolean active;
    private MonitoredInt currentWork;
    private MonitoredInt maxWork; //TODO: Change based on recipe

    private boolean lastActive;

    public TileEntityAlloyMixer() {

    }

    @Override
    protected void createSyncedFields() {
        energyStorage = new SyncableEnergyStorage(Config.EnergyStorage.ALLOY_MIXER);
        active = new MonitoredBoolean(false);
        currentWork = new MonitoredInt(0);
        maxWork = new MonitoredInt(200);

        battery = new SyncableItemStack();

        input1 = new SyncableItemStack();
        input2 = new SyncableItemStack();
        input3 = new SyncableItemStack();
        output = new SyncableItemStack();
    }

    public int getWorkProgressScaled(int max) {
        return (int)(((double)getCurrentWork() / (double)getMaxWork()) * max);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        setActive(false);

        ItemStack[] inputSlots = new ItemStack[3];

        inputSlots[0] = input1.get() != null ? input1.get().copy() : null;
        inputSlots[1] = input2.get() != null ? input2.get().copy() : null;
        inputSlots[2] = input3.get() != null ? input3.get().copy() : null;

        if(RecipesAlloyMixer.isValidRecipe(inputSlots)) {
            if (getEnergyStorage().getEnergyStored() >= Config.EnergyUsage.ALLOY_MIXER) {
                if (getCurrentWork() < getMaxWork()) {
                    setCurrentWork(getCurrentWork() + 1);
                    getEnergyStorage().extractEnergy(Config.EnergyUsage.ALLOY_MIXER, false);
                    setActive(true);
                }

                if (getCurrentWork() >= getMaxWork()) {
                    ItemStack[] craftResult = RecipesAlloyMixer.doCraft(inputSlots);
                    if (output.get() == null || output.get().stackSize == 0) {
                        input1.set(craftResult[0]);
                        input2.set(craftResult[1]);
                        input3.set(craftResult[2]);
                        output.set(craftResult[3]);
                        setCurrentWork(0);
                    } else if (craftResult[3].getItem().equals(output.get().getItem())
                            && craftResult[3].getItemDamage() == output.get().getItemDamage()
                            && craftResult[3].stackSize + output.get().stackSize <= output.get().getMaxStackSize()) {

                        input1.set(craftResult[0]);
                        input2.set(craftResult[1]);
                        input3.set(craftResult[2]);

                        ItemStack stack = output.get().copy();
                        stack.stackSize += craftResult[3].stackSize;
                        output.set(stack);
                        setCurrentWork(0);
                    }
                }
            }
        }

        lastActive = isActive();
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
                return input3;
            case 4:
                return output;
            default:
                return battery;
        }
    }

    @Override
    public Object getServerGui(EntityPlayer player) {
        return new ContainerAlloyMixer(player.inventory, this);
    }

    @Override
    public Object getClientGui(EntityPlayer player) {
        return new GuiAlloyMixer(new ContainerAlloyMixer(player.inventory, this));
    }

    @Override
    public boolean canOpenGui(EntityPlayer player) {
        return true;
    }

    public <T> void subscribeListener(String name, IValueChangedListener<T> listener) {
        if(name.equalsIgnoreCase("energyStored") || name.equalsIgnoreCase("maxEnergyStored") || name.equalsIgnoreCase("maxReceive") || name.equalsIgnoreCase("maxExtract")) {
            energyStorage.subscribe(name, listener);
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

    @Override
    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        player.addChatComponentMessage(new ChatComponentText("Meta: " + blockMetadata));
        return true;
    }

    @Override
    public IInventory getInventory() {
        return inventory;
    }
}
