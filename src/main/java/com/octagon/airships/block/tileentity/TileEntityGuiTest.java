package com.octagon.airships.block.tileentity;

import cofh.api.energy.IEnergyReceiver;
import com.octagon.airships.MassEffectShips;
import com.octagon.airships.block.item.ItemMachine;
import com.octagon.airships.client.gui.machine.ContainerMachineBase;
import com.octagon.airships.client.gui.machine.GuiOpenMachineBase;
import com.octagon.airships.recipe.RecipesAlloyMixer;
import com.octagon.airships.reference.Config;
import com.octagon.airships.reference.GUIs;
import com.octagon.airships.sync.SyncableEnergyStorage;
import com.octagon.airships.sync.SyncableItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import openmods.api.IActivateAwareTile;
import openmods.api.IHasGui;
import openmods.gui.listener.IValueChangedListener;
import openmods.sync.SyncableBoolean;
import openmods.sync.SyncableInt;
import openmods.sync.drops.StoreOnDrop;

public class TileEntityGuiTest extends TileEntityMachineBase implements IHasGui, IActivateAwareTile, IEnergyReceiver, ISidedInventory {
    private SyncableItemStack battery;

    private SyncableItemStack input1;
    private SyncableItemStack input2;
    private SyncableItemStack input3;
    private SyncableItemStack output;

    @StoreOnDrop(name = ItemMachine.ENERGY_STORAGE_TAG)
    private SyncableEnergyStorage energyStorage;
    private SyncableBoolean active;
    private SyncableInt currentWork;
    private SyncableInt maxWork; //TODO: Change based on recipe

    private boolean lastActive;

    public TileEntityGuiTest() {
        super("Alloy Mixer");
    }

    @Override
    protected void createSyncedFields() {
        energyStorage = new SyncableEnergyStorage(Config.EnergyStorage.ALLOY_MIXER);
        active = new SyncableBoolean(false);
        currentWork = new SyncableInt(0);
        maxWork = new SyncableInt(200);

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

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        if(slot == 0) {
            super.setInventorySlotContents(slot, itemStack);
            return;
        }
        if(slot > 4) return;

        if(itemStack != null) {
            if (itemStack.stackSize > getInventoryStackLimit())
                itemStack.stackSize = getInventoryStackLimit();
        }

        getSlot(slot).set(itemStack);
    }

    @Override
    public int getSizeInventory() {
        return 4 + super.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if(slot == 0) return super.getStackInSlot(slot);

        switch (slot) {
            case 1:
                return input1.get();
            case 2:
                return input2.get();
            case 3:
                return input3.get();
            case 4:
                return output.get();
        }

        return null;
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

        if(slot > 4) return null;
        SyncableItemStack item = getSlot(slot);

        if(item.get() == null) return null;


        if(item.get().stackSize <= amount) {
            ItemStack itemStack = item.get().copy();
            item.set(null);
            return itemStack;
        } else {
            item.decreaseStackSize(amount);
            return new ItemStack(item.get().getItem(), amount, item.get().getItemDamage());
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
    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(!worldObj.isRemote) return true;

        if(!player.isSneaking())
            player.openGui(MassEffectShips.instance, GUIs.DYNAMIC.ordinal(), worldObj, xCoord, yCoord, zCoord);
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

    @Override
    public <T> void subscribeListener(String name, IValueChangedListener<T> listener) {
        if(name.equalsIgnoreCase("energyStored") || name.equalsIgnoreCase("maxEnergyStored") || name.equalsIgnoreCase("maxReceive") || name.equalsIgnoreCase("maxExtract")) {
            energyStorage.subscribeListener(name, listener);
        }
    }
}
