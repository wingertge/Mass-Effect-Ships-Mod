package com.octagon.airships.sync;

import com.google.common.collect.Sets;
import net.minecraft.nbt.NBTTagCompound;
import openmods.gui.listener.IValueChangedListener;
import openmods.sync.SyncableObjectBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

public class MonitoredInt extends SyncableObjectBase implements IMonitoredValue<Integer> {
    private int value = 0;
    private Set<IValueChangedListener<Integer>> listeners = Sets.newHashSet();

    public MonitoredInt() {

    }

    public MonitoredInt(int i) {
        value = i;
    }

    @Override
    public void subscribe(IValueChangedListener<Integer> listener) {
        listeners.add(listener);
    }

    @Override
    public void unsubscribe(IValueChangedListener<Integer> listener) {
        listeners.remove(listener);
    }

    @Override
    public Integer get() {
        return value;
    }

    @Override
    public void set(Integer value) {
        this.value = value;
        fireListeners();
    }

    @Override
    public void forceUpdate() {
        fireListeners();
    }

    private void fireListeners() {
        for(IValueChangedListener<Integer> listener : listeners) {
            listener.valueChanged(value);
        }
    }

    public void modify(int change) {
        this.value += change;
        fireListeners();
    }

    @Override
    public void readFromStream(DataInputStream stream) throws IOException {
        value = stream.readInt();
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
}
