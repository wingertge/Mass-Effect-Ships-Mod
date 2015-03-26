package com.octagon.airships.sync;

import cofh.api.energy.IEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import openmods.gui.listener.IValueChangedListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

public class SyncableEnergyStorage extends SyncableObjectBase<SyncableEnergyStorage> implements IEnergyStorage {
    protected MonitoredInt energyStored;
    protected MonitoredInt maxEnergyStored;
    protected MonitoredInt maxReceive;
    protected MonitoredInt maxExtract;

    public SyncableEnergyStorage(int capacity) {

        this(capacity, capacity, capacity);
    }

    public SyncableEnergyStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }

    public SyncableEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        createMonitoredValues();
        this.maxEnergyStored.set(capacity);
        this.maxReceive.set(maxReceive);
        this.maxExtract.set(maxExtract);
    }

    private void createMonitoredValues() {
        energyStored = new MonitoredInt();
        maxEnergyStored = new MonitoredInt();
        maxReceive = new MonitoredInt();
        maxExtract = new MonitoredInt();
    }

    @Override
    public void readFromStream(DataInputStream stream) throws IOException {
        setMaxEnergyStored(stream.readInt());
        setMaxReceive(stream.readInt());
        setMaxExtract(stream.readInt());
        setEnergyStored(stream.readInt());
    }

    @Override
    public void writeToStream(DataOutputStream stream) throws IOException {
        stream.writeInt(getMaxEnergyStored());
        stream.writeInt(getMaxReceive());
        stream.writeInt(getMaxExtract());
        stream.writeInt(getEnergyStored());
    }

    @Override
    public void writeToNBT(NBTTagCompound tag, String name) {
        NBTTagCompound compound = new NBTTagCompound();
        if (energyStored.get() < 0) {
            energyStored.set(0);
        }
        compound.setInteger("Energy", energyStored.get());

        tag.setTag(name, compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, String name) {
        NBTTagCompound compound = tag.getCompoundTag(name);

        this.energyStored.set(compound.getInteger("Energy"));

        if (energyStored.get() > maxEnergyStored.get()) {
            energyStored.set(maxEnergyStored.get());
        }
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(maxEnergyStored.get() - energyStored.get(), Math.min(this.maxReceive.get(), maxReceive));

        if (!simulate) {
            energyStored.modify(energyReceived);
        }

        markDirty();
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(energyStored.get(), Math.min(this.maxExtract.get(), maxExtract));

        if (!simulate) {
            energyStored.modify(-energyExtracted);
        }

        markDirty();
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return energyStored.get();
    }

    @Override
    public int getMaxEnergyStored() {
        return maxEnergyStored.get();
    }

    public void setEnergyStored(int energy) {
        this.energyStored.set(energy);

        if (this.energyStored.get() > maxEnergyStored.get()) {
            this.energyStored.set(maxEnergyStored.get());
        } else if (this.energyStored.get() < 0) {
            this.energyStored.set(0);
        }

        markDirty();
    }

    public void modifyEnergyStored(int energy) {

        this.energyStored.modify(energy);

        if (this.energyStored.get() > maxEnergyStored.get()) {
            this.energyStored.set(maxEnergyStored.get());
        } else if (this.energyStored.get() < 0) {
            this.energyStored.set(0);
        }

        markDirty();
    }

    public void setMaxEnergyStored(int maxEnergyStored) {
        this.maxEnergyStored.set(maxEnergyStored);
        markDirty();
    }

    public int getMaxExtract() {
        return maxExtract.get();
    }

    public void setMaxExtract(int maxExtract) {
        this.maxExtract.set(maxExtract);

        markDirty();
    }

    public int getMaxReceive() {
        return maxReceive.get();
    }

    public void setMaxReceive(int maxReceive) {
        this.maxReceive.set(maxReceive);

        markDirty();
    }

    public <T> void subscribeListener(String name, IValueChangedListener<T> listener) {
        Field[] fields = getClass().getDeclaredFields();
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

    public <T> void unsubscribeListener(String name, IValueChangedListener<T> listener) {
        for(Field field : SyncableEnergyStorage.class.getFields()) {
            boolean implementsInterface = Arrays.asList(field.getType().getInterfaces()).contains(IMonitoredValue.class);
            if(field.getName().equalsIgnoreCase(name) && implementsInterface)
                try {
                    ((IMonitoredValue)field.get(this)).unsubscribe(listener);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void assign(SyncableEnergyStorage value) {
        energyStored = value.energyStored;
        maxEnergyStored = value.maxEnergyStored;
        maxReceive = value.maxReceive;
        maxExtract = value.maxExtract;
        markDirty();
    }
}
