package com.octagon.airships.sync;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SyncableCoord extends SyncableObjectBase<SyncableCoord> {
    private int dimensionId;
    private int x;
    private int y;
    private int z;

    public SyncableCoord() {
        dimensionId = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public SyncableCoord(World world, int x, int y, int z) {
        dimensionId = world != null ? world.provider.dimensionId : -1;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public World getWorld() {
        return DimensionManager.getWorld(dimensionId);
    }

    public void setWorld(World world) {
        this.dimensionId = world.provider.dimensionId;
        markDirty();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        markDirty();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        markDirty();
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
        markDirty();
    }

    @Override
    public void readFromStream(DataInputStream stream) throws IOException {
        dimensionId = stream.readInt();
        x = stream.readInt();
        y = stream.readInt();
        z = stream.readInt();
    }

    @Override
    public void writeToStream(DataOutputStream stream) throws IOException {
        stream.writeInt(dimensionId);
        stream.writeInt(x);
        stream.writeInt(y);
        stream.writeInt(z);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, String name) {
        NBTTagCompound coordCompound = new NBTTagCompound();
        coordCompound.setInteger("dimensionId", dimensionId);
        coordCompound.setInteger("x", x);
        coordCompound.setInteger("y", y);
        coordCompound.setInteger("z", z);

        nbt.setTag(name, coordCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt, String name) {
        NBTTagCompound coordCompound = nbt.getCompoundTag(name);

        this.dimensionId = coordCompound.getInteger("dimensionId");
        this.x = coordCompound.getInteger("x");
        this.y = coordCompound.getInteger("y");
        this.z = coordCompound.getInteger("z");
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof SyncableCoord)) return false;

        SyncableCoord coord = (SyncableCoord)obj;
        return coord.dimensionId == dimensionId && coord.getX() == x && coord.getY() == y && coord.getZ() == z;
    }

    public void assign(SyncableCoord coord) {
        dimensionId = coord.dimensionId;
        x = coord.x;
        y = coord.y;
        z = coord.z;
        markDirty();
    }
}
