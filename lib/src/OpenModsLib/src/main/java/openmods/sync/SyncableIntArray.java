package openmods.sync;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;

public class SyncableIntArray extends SyncableObjectBase implements ISyncableValueProvider<int[]> {

	private int[] value;

	public SyncableIntArray(int[] value) {
		this.value = value;
	}

	public SyncableIntArray() {
		this(new int[0]);
	}

	public void setValue(int[] newValue) {
		if (!Arrays.equals(value, newValue)) {
			value = newValue;
			markDirty();
		}
	}

	public void setValue(int offset, int newValue) {
		if (value[offset] != newValue) {
			value[offset] = newValue;
			markDirty();
		}
	}

	public int getValue(int offset) {
		return value[offset];
	}

	@Override
	public int[] getValue() {
		return value;
	}

	public int size() {
		if (value == null) { return 0; }
		return value.length;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public void readFromStream(DataInputStream stream) throws IOException {
		int length = stream.readInt();
		value = new int[length];
		for (int i = 0; i < length; i++) {
			value[i] = stream.readInt();
		}
	}

	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeInt(size());
		for (int i = 0; i < size(); i++) {
			stream.writeInt(value[i]);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag, String name) {
		tag.setIntArray(name, value);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag, String name) {
		value = tag.getIntArray(name);
	}

	public void clear() {
		value = new int[0];
		markDirty();
	}
}
