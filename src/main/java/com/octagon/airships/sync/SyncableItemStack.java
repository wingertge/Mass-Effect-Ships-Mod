package com.octagon.airships.sync;

import com.google.common.io.ByteStreams;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import openmods.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SyncableItemStack extends SyncableObjectBase<SyncableItemStack> {
    private static final String TAG_TAG = "tag";
    private static final String TAG_DAMAGE = "Damage";
    private static final String TAG_COUNT = "Count";
    private static final String TAG_ID = "id";
    private ItemStack stack;

    @Override
    public void readFromStream(DataInputStream stream) throws IOException {
        int length = ByteUtils.readVLI(stream);
        if (length > 0) {
            int itemId = stream.readInt();
            byte size = stream.readByte();
            short damage = stream.readShort();

            NBTTagCompound deserialized = new NBTTagCompound();
            deserialized.setInteger(TAG_ID, itemId);
            deserialized.setByte(TAG_COUNT, size);
            deserialized.setShort(TAG_DAMAGE, damage);

            length--;
            if (length > 0) {
                // GZIP stream reads more than needed -> needs bounding if we want to reuse stream
                NBTTagCompound tag = CompressedStreamTools.readCompressed(ByteStreams.limit(stream, length));
                deserialized.setTag(TAG_TAG, tag);
            }

            this.stack = ItemStack.loadItemStackFromNBT(deserialized);
        } else {
            this.stack = null;
        }
    }

    @Override
    public void writeToStream(DataOutputStream stream) throws IOException {
        if (stack != null) {
            NBTTagCompound serialized = new NBTTagCompound();
            stack.writeToNBT(serialized);
            int id = serialized.getInteger(TAG_ID);
            byte size = serialized.getByte(TAG_COUNT);
            short damage = serialized.getShort(TAG_DAMAGE);

            int payloadSize = 1;
            byte[] tagBytes = null;
            if (serialized.hasKey(TAG_TAG)) {
                NBTTagCompound tag = serialized.getCompoundTag(TAG_TAG);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                CompressedStreamTools.writeCompressed(tag, buffer);

                tagBytes = buffer.toByteArray();
                payloadSize += tagBytes.length;
            }

            ByteUtils.writeVLI(stream, payloadSize);
            stream.writeInt(id);
            stream.writeByte(size);
            stream.writeShort(damage);

            if (tagBytes != null) stream.write(tagBytes);
        } else {
            stream.writeByte(0);
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, String name) {
        if (stack != null) {
            NBTTagCompound serialized = new NBTTagCompound();
            stack.writeToNBT(serialized);
            nbt.setTag(name, serialized);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt, String name) {
        if (nbt.hasKey(name)) {
            NBTTagCompound serialized = nbt.getCompoundTag(name);
            stack = ItemStack.loadItemStackFromNBT(serialized);
        } else {
            stack = null;
        }
    }

    public void set(ItemStack stack) {
        this.stack = stack != null ? stack.copy() : null;
        markDirty();
    }

    public ItemStack get() {
        return stack;
    }

    public void setStackSize(int size) {
        stack.stackSize = size;
        markDirty();
    }

    public void increaseStackSize(int size) {
        stack.stackSize += size;
        markDirty();
    }

    public void decreaseStackSize(int size) {
        stack.stackSize += size;
        markDirty();
    }

    @Override
    public void assign(SyncableItemStack value) {
        stack = value.stack;
        markDirty();
    }
}
