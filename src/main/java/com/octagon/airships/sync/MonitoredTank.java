package com.octagon.airships.sync;

import com.google.common.collect.Sets;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import openmods.gui.listener.IValueChangedListener;
import openmods.sync.SyncableTank;

import java.util.Set;

public class MonitoredTank extends SyncableTank implements IMonitoredValue<FluidStack> {
    private Set<IValueChangedListener<FluidStack>> listeners = Sets.newHashSet();

    public MonitoredTank(int capacity, FluidStack... acceptableFluids) {
        super(capacity, acceptableFluids);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        FluidStack drained = super.drain(maxDrain, doDrain);
        if(doDrain && drained != null) fireListeners();
        return drained;
    }

    @Override
    public FluidStack drain(FluidStack stack, boolean doDrain) {
        FluidStack drained = super.drain(stack, doDrain);
        if(doDrain && drained != null) fireListeners();
        return drained;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        int filled = super.fill(resource, doFill);
        if(doFill && filled > 0) fireListeners();
        return filled;
    }

    @Override
    public FluidStack get() {
        return getValue();
    }

    @Override
    public void set(FluidStack value) {
        setFluid(value);
        markDirty();
        fireListeners();
    }

    @Override
    public void forceUpdate() {
        fireListeners();
    }

    @Override
    public void subscribe(IValueChangedListener<FluidStack> listener) {
        listeners.add(listener);
    }

    @Override
    public void unsubscribe(IValueChangedListener<FluidStack> listener) {
        listeners.remove(listener);
    }

    private void fireListeners() {
        listeners.stream().forEach(a -> a.valueChanged(getValue()));
    }

    @Override
    public FluidStack getValue() {
        return getFluid();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, String name) {
        NBTTagCompound compound = tag.getCompoundTag(name);
        super.readFromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag, String name) {
        NBTTagCompound compound = new NBTTagCompound();
        super.writeToNBT(compound);
        tag.setTag(name, compound);
    }
}
