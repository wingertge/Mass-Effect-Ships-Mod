package com.octagon.airships.sync;

import openmods.gui.listener.IValueChangedListener;

public interface IMonitoredComposite {
    <T> void subscribe(String valueName, IValueChangedListener<T> listener, String... callChain);
    <T> void unsubscribe(String valueName, IValueChangedListener<T> listener, String... callChain);
    void forceUpdate();
}
