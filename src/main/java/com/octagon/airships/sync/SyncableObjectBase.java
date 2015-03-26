package com.octagon.airships.sync;

public abstract class SyncableObjectBase<T extends SyncableObjectBase> extends openmods.sync.SyncableObjectBase {
    public abstract void assign(T value);
}
