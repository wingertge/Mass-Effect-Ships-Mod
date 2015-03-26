package openmods.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import openmods.utils.io.IStreamReader;
import openmods.utils.io.IStreamWriter;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

public class CollectionUtils {

	public static final Random rnd = new Random();

	public static <T> T getFirst(Collection<T> collection) {
		Preconditions.checkArgument(!collection.isEmpty(), "Collection cannot be empty");
		return collection.iterator().next();
	}

	public static <T> T getRandom(Collection<T> collection) {
		return getRandom(collection, rnd);
	}

	public static <T> T getRandom(Collection<T> collection, Random rand) {
		final int size = collection.size();
		Preconditions.checkArgument(size > 0, "Can't select from empty collection");
		if (size == 1) return getFirst(collection);
		int randomIndex = rnd.nextInt(size);
		int i = 0;
		for (T obj : collection) {
			if (i == randomIndex) return obj;
			i = i + 1;
		}
		return null;
	}

	public static <T> T getRandom(List<T> list) {
		return getRandom(list, rnd);
	}

	public static <T> T getRandom(List<T> list, Random rand) {
		final int size = list.size();
		Preconditions.checkArgument(size > 0, "Can't select from empty list");
		if (size == 0) return null;
		if (size == 1) return list.get(0);
		int randomIndex = rnd.nextInt(list.size());
		return list.get(randomIndex);
	}

	public static <T> T getWeightedRandom(Map<T, Integer> collection) {
		int totalWeight = 0;
		Collection<Integer> values = collection.values();
		for (Integer i : values) {
			totalWeight += i;
		}

		int r = rnd.nextInt(totalWeight);
		for (Entry<T, Integer> entry : collection.entrySet()) {
			r -= entry.getValue();
			if (r <= 0) { return entry.getKey(); }
		}
		return null;
	}

	public static void readSortedIdList(DataInput input, Collection<Integer> output) {
		int elemCount = ByteUtils.readVLI(input);

		int currentId = 0;
		for (int i = 0; i < elemCount; i++) {
			currentId += ByteUtils.readVLI(input);
			output.add(currentId);
		}
	}

	public static void writeSortedIdList(DataOutput output, SortedSet<Integer> idList) {
		ByteUtils.writeVLI(output, idList.size());

		int currentId = 0;
		for (Integer id : idList) {
			int delta = id - currentId;
			ByteUtils.writeVLI(output, delta);
			currentId = id;
		}
	}

	public static <D> void readSortedIdMap(DataInput input, Map<Integer, D> output, IStreamReader<D> reader) {
		int elemCount = ByteUtils.readVLI(input);

		int currentId = 0;
		try {
			for (int i = 0; i < elemCount; i++) {
				currentId += ByteUtils.readVLI(input);
				D data = reader.readFromStream(input);
				output.put(currentId, data);
			}
		} catch (IOException e) {
			Throwables.propagate(e);
		}
	}

	public static <D> void writeSortedIdMap(DataOutput output, SortedMap<Integer, D> input, IStreamWriter<D> writer) {
		ByteUtils.writeVLI(output, input.size());

		int currentId = 0;
		try {
			for (Map.Entry<Integer, D> e : input.entrySet()) {
				final int id = e.getKey();
				final int delta = id - currentId;
				ByteUtils.writeVLI(output, delta);
				writer.writeToStream(e.getValue(), output);
				currentId = id;
			}
		} catch (IOException e) {
			Throwables.propagate(e);
		}
	}
}
