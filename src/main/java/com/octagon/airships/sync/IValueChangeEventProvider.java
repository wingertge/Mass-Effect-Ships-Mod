package com.octagon.airships.sync;

import openmods.gui.listener.IValueChangedListener;

public interface IValueChangeEventProvider<T extends Object> {
    public void subscribe(IValueChangedListener<T> listener);
    public void unsubscribe(IValueChangedListener<T> listener);
}
