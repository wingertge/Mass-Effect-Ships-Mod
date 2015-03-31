package com.octagon.airships.sync;

public interface IMonitoredValue<T extends Object> extends IValueChangeEventProvider<T> {
    T get();
    void set(T value);
    void forceUpdate();
}
