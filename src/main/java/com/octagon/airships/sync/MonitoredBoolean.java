package com.octagon.airships.sync;

import com.google.common.collect.Sets;
import net.minecraft.nbt.NBTTagCompound;
import openmods.gui.listener.IValueChangedListener;
import openmods.sync.SyncableObjectBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

public class MonitoredBoolean extends SyncableObjectBase implements IMonitoredValue<Boolean> {
    private boolean value;
    private Set<IValueChangedListener<Boolean>> listeners = Sets.newHashSet();

    public MonitoredBoolean(boolean value) {
        this.value = value;
    }

    public MonitoredBoolean() {}

    public void set(Boolean newValue) {
        if (newValue != value) {
            value = newValue;
            markDirty();
            fireListeners();
        }
    }

    @Override
    public void forceUpdate() {
        fireListeners();
    }

    public Boolean get() {
        return value;
    }

    @Override
    public void readFromStream(DataInputStream stream) throws IOException {
        value = stream.readBoolean();
        fireListeners();
    }

    @Override
    public void writeToStream(DataOutputStream stream) throws IOException {
        stream.writeBoolean(value);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag, String name) {
        tag.setBoolean(name, value);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, String name) {
        value = tag.getBoolean(name);
        fireListeners();
    }

    public void toggle() {
        value = !value;
        markDirty();
        fireListeners();
    }

    @Override
    public void subscribe(IValueChangedListener<Boolean> listener) {
        listeners.add(listener);
    }

    @Override
    public void unsubscribe(IValueChangedListener<Boolean> listener) {
        listeners.remove(listener);
    }

    private void fireListeners() {
        for(IValueChangedListener<Boolean> listener : listeners) {
            listener.valueChanged(value);
        }
    }
}
