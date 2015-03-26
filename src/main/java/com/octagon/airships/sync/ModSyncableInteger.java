package com.octagon.airships.sync;

import com.google.common.collect.Sets;
import net.minecraft.nbt.NBTTagCompound;
import openmods.gui.listener.IValueChangedListener;
import openmods.sync.ISyncableValueProvider;
import openmods.sync.SyncableObjectBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

public class ModSyncableInteger extends SyncableObjectBase implements ISyncableValueProvider<Integer>, IValueChangeEventProvider<Integer> {
    protected int value = 0;
    private Set<IValueChangedListener<Integer>> listeners = Sets.newHashSet();

    public ModSyncableInteger(int value) {
        this.value = value;
    }

    public ModSyncableInteger() {}


    @Override
    public void readFromStream(DataInputStream stream) throws IOException {
        value = stream.readInt();
    }

    public void modify(int by) {
        set(value + by);
    }

    public void set(int val) {
        if (val != value) {
            value = val;
            markDirty();
        }
    }

    public int get() {
        return value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void writeToStream(DataOutputStream stream) throws IOException {
        stream.writeInt(value);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag, String name) {
        tag.setInteger(name, value);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, String name) {
        if (tag.hasKey(name)) {
            value = tag.getInteger(name);
        }
    }

    public void subscribe(IValueChangedListener<Integer> listener) {
        this.listeners.add(listener);
    }

    public void unsubscribe(IValueChangedListener<Integer> listener) {
        this.listeners.remove(listener);
    }
}
