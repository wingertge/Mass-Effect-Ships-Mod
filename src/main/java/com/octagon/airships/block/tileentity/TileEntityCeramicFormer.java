package com.octagon.airships.block.tileentity;

import com.octagon.airships.MassEffectShips;
import com.octagon.airships.block.item.ItemMachine;
import com.octagon.airships.client.gui.machine.ContainerCeramicFormer;
import com.octagon.airships.client.gui.machine.GuiCeramicFormer;
import com.octagon.airships.recipe.RecipesCeramicFormer;
import com.octagon.airships.reference.Config;
import com.octagon.airships.reference.GUIs;
import com.octagon.airships.sync.IMonitoredValue;
import com.octagon.airships.sync.MonitoredInt;
import com.octagon.airships.sync.SyncableEnergyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.api.IHasGui;
import openmods.gui.listener.IValueChangedListener;
import openmods.include.IncludeInterface;
import openmods.inventory.GenericInventory;
import openmods.inventory.TileEntityInventory;
import openmods.sync.SyncableBoolean;
import openmods.sync.SyncableSides;
import openmods.sync.drops.StoreOnDrop;
import openmods.utils.SidedInventoryAdapter;

import java.lang.reflect.Field;
import java.util.Arrays;

public class TileEntityCeramicFormer extends TileEntityMachineBase implements IHasGui {

    @StoreOnDrop(name = ItemMachine.ENERGY_STORAGE_TAG)
    private SyncableEnergyStorage energyStorage;
    private SyncableBoolean active;
    private GenericInventory inventory = registerInventoryCallback(new TileEntityInventory(this, "ceramicFormer", false, 4));

    private MonitoredInt currentWork;
    private MonitoredInt maxWork;
    private boolean lastActive = false;

    private SyncableSides batterySides;
    private SyncableSides input1Sides;
    private SyncableSides input2Sides;
    private SyncableSides outputSides;

    @IncludeInterface(ISidedInventory.class)
    private SidedInventoryAdapter sidedInventory = new SidedInventoryAdapter(inventory);

    public TileEntityCeramicFormer() {
        super();
        sidedInventory.registerSlot(0, batterySides, true, true);
        sidedInventory.registerSlot(1, input1Sides, true, true);
        sidedInventory.registerSlot(2, input2Sides, true, true);
        sidedInventory.registerSlot(3, outputSides, false, true);
    }

    @Override
    protected void createSyncedFields() {
        energyStorage = new SyncableEnergyStorage(Config.EnergyStorage.CERAMIC_FORMER);
        active = new SyncableBoolean(false);

        currentWork = new MonitoredInt(0);
        maxWork = new MonitoredInt(200);

        batterySides = new SyncableSides();
        input1Sides = new SyncableSides();
        input2Sides = new SyncableSides();
        outputSides = new SyncableSides();

        for(int i = 0; i < 6; i++) {
            batterySides.set(ForgeDirection.getOrientation(i), true);
            input1Sides.set(ForgeDirection.getOrientation(i), true);
            input2Sides.set(ForgeDirection.getOrientation(i), true);
            outputSides.set(ForgeDirection.getOrientation(i), true);
        }
    }

    public int getWorkProgressScaled(int max) {
        return (int)(((double)getCurrentWork() / (double)getMaxWork()) * max);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        setActive(false);

        ItemStack input1 = getInventory().getStackInSlot(1);
        ItemStack input2 = getInventory().getStackInSlot(2);
        if(input1 != null && input2 != null) {
            if (RecipesCeramicFormer.isValid(input1.copy(), input2.copy())) {
                setMaxWork(RecipesCeramicFormer.getWorkForItem(input2.copy()));

                if (getEnergyStorage().getEnergyStored() >= Config.EnergyUsage.CERAMIC_FORMER) {
                    if (getCurrentWork() < getMaxWork()) {
                        inc(currentWork);
                        getEnergyStorage().extractEnergy(Config.EnergyUsage.CERAMIC_FORMER, false);
                        setActive(true);
                    }

                    ItemStack[] inputSlots = new ItemStack[2];
                    inputSlots[0] = input1.copy();
                    inputSlots[1] = input2.copy();

                    if (getCurrentWork() >= getMaxWork()) {
                        ItemStack[] craftResult = RecipesCeramicFormer.doCraft(inputSlots);
                        if (craftResult.length != 3) return;
                        ItemStack output = getInventory().getStackInSlot(3);
                        if (output == null || output.stackSize == 0) {
                            input1 = craftResult[0];
                            input2 = craftResult[1];
                            output = craftResult[2];

                            setCurrentWork(0);
                        } else if (craftResult[2].getItem().equals(output.getItem())
                                && craftResult[2].getItemDamage() == output.getItemDamage()
                                && craftResult[2].stackSize + output.stackSize <= output.getMaxStackSize()) {

                            input1 = craftResult[0];
                            input2 = craftResult[1];
                            output.stackSize = craftResult[2].stackSize;

                            setCurrentWork(0);
                        }
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

    private void inc(MonitoredInt i) {
        i.set(i.get() + 1);
    }

    @Override
    public boolean canOpenGui(EntityPlayer player) {
        return true;
    }

    @Override
    public Object getClientGui(EntityPlayer player) {
        return new GuiCeramicFormer(new ContainerCeramicFormer(player.inventory, this));
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
    public IInventory getInventory() {
        return inventory;
    }
}
