package com.octagon.airships.block.fluid;

import com.google.common.collect.Sets;
import com.octagon.airships.block.tileentity.TileEntityLaunchPad;
import com.octagon.airships.reference.Config;
import com.octagon.airships.sync.IMonitoredValue;
import com.octagon.airships.sync.MonitoredTank;
import com.octagon.airships.util.InvalidOperationException;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import openmods.gui.listener.IValueChangedListener;

import java.util.List;
import java.util.Set;

public class LaunchPadFluidTank extends MonitoredTank implements IFluidTank, IMonitoredValue<FluidStack>, IValueChangedListener<FluidStack> {
    private final int id;
    private List<TileEntityLaunchPad> launchPads;
    private Set<IValueChangedListener<FluidStack>> listeners = Sets.newHashSet();

    public LaunchPadFluidTank(int id, List<TileEntityLaunchPad> launchPads) {
        super(Config.FluidStorage.LAUNCH_PAD * launchPads.size());
        this.id = id;
        setLaunchPads(launchPads);
    }

    public void setLaunchPads(List<TileEntityLaunchPad> launchPads) {
        this.launchPads = launchPads;
        launchPads.parallelStream().forEach(a -> a.getTank(id).subscribe(this));
    }

    @Override
    public FluidStack getFluid() {
        if(launchPads.size() == 0) return null;
        int amount = launchPads.stream().map(a -> a.getTank(id).getFluidAmount()).reduce(0, (a, b) -> a + b);
        if(amount == 0) return null;
        Fluid fluid = launchPads.stream().filter(a -> a.getTank(id).getFluid() != null).map(a -> a.getTank(id).getFluid().getFluid()).findFirst().orElse(null);
        if(fluid != null) return new FluidStack(fluid, amount);
        else return null;
    }

    @Override
    public int getFluidAmount() {
        return launchPads.stream().map(a -> a.getTank(id).getFluidAmount()).reduce(0, (a, b) -> a + b);
    }

    @Override
    public int getCapacity() {
        return Config.FluidStorage.LAUNCH_PAD * launchPads.size();
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(getFluid(), getCapacity());
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        try {
            throw new InvalidOperationException("IT'S A GHOST TANK, YOU IDIOT!");
        } catch (InvalidOperationException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        try {
            throw new InvalidOperationException("IT'S A GHOST TANK, YOU IDIOT!");
        } catch (InvalidOperationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FluidStack get() {
        return getFluid();
    }

    @Override
    public void set(FluidStack value) {
        try {
            throw new InvalidOperationException("IT'S A GHOST TANK, YOU IDIOT!");
        } catch (InvalidOperationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void forceUpdate() {
        fireListeners();
    }

    private void fireListeners() {
        FluidStack fluid = getFluid();
        listeners.forEach(a -> a.valueChanged(fluid));
    }

    @Override
    public void subscribe(IValueChangedListener<FluidStack> listener) {
        listeners.add(listener);
    }

    @Override
    public void unsubscribe(IValueChangedListener<FluidStack> listener) {
        listeners.remove(listener);
    }

    @Override
    public void valueChanged(FluidStack value) {
        markDirty();
        fireListeners();
    }
}
