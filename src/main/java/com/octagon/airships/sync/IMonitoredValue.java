package com.octagon.airships.sync;

public interface IMonitoredValue<T extends Object> extends IValueChangeEventProvider<T> {
    public T get();
    public void set(T value);
}
