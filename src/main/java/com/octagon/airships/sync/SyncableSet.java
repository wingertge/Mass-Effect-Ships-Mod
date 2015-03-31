package com.octagon.airships.sync;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import openmods.sync.ISyncableObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SyncableSet<T extends ISyncableObject> extends HashSet<T> implements ISyncableObject {
    private boolean dirty;
    private Class<T> clazz;

    public SyncableSet(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void markClean() {
        dirty = false;
    }

    @Override
    public void markDirty() {
        dirty = true;
    }

    @Override
    public void readFromStream(DataInputStream stream) throws IOException {
        super.clear();
        int count = stream.readInt();
        for(int i = 0; i < count; i++) {
            try {
                T value = clazz.newInstance();
                value.readFromStream(stream);
                super.add(value);
            } catch (IOException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void writeToStream(DataOutputStream stream) throws IOException {
        stream.writeInt(super.size());
        super.forEach(a -> {
            try {
                a.writeToStream(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, String name) {
        NBTTagList list = new NBTTagList();

        super.forEach(a -> {
            NBTTagCompound tag = new NBTTagCompound();
            a.writeToNBT(tag, "Value");
            list.appendTag(tag);
        });

        nbt.setTag(name, list);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt, String name) {
        super.clear();
        NBTTagList list = nbt.getTagList(name, 10);

        try {
            for(int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                T value = clazz.newInstance();
                value.readFromNBT(tag, "Value");
                super.add(value);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        markDirty();
        super.forEach(action);
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        markDirty();
        return super.removeIf(filter);
    }

    @Override
    public Stream<T> stream() {
        markDirty();
        return super.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        markDirty();
        return super.parallelStream();
    }

    @Override
    public boolean add(T t) {
        markDirty();
        return super.add(t);
    }

    @Override
    public void clear() {
        markDirty();
        super.clear();
    }

    @Override
    public boolean remove(Object o) {
        markDirty();
        return super.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        markDirty();
        return super.removeAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        markDirty();
        return super.addAll(c);
    }
}
