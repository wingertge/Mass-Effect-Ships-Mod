package openmods.fakeplayer;

import java.util.Map;
import java.util.Queue;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import openmods.LibConfig;
import openmods.Log;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class FakePlayerPool {

	public interface PlayerUser {
		public void usePlayer(OpenModsFakePlayer fakePlayer);
	}

	public interface PlayerUserReturning<T> {
		public T usePlayer(OpenModsFakePlayer fakePlayer);
	}

	private static PlayerUserReturning<Void> wrap(final PlayerUser user) {
		return new PlayerUserReturning<Void>() {
			@Override
			public Void usePlayer(OpenModsFakePlayer fakePlayer) {
				user.usePlayer(fakePlayer);
				return null;
			}
		};
	}

	private static class WorldPool {
		private final Queue<OpenModsFakePlayer> pool = new ConcurrentLinkedQueue<OpenModsFakePlayer>();
		private final AtomicInteger playerCount = new AtomicInteger();

		public <T> T executeOnPlayer(WorldServer world, PlayerUserReturning<T> user) {
			OpenModsFakePlayer player = pool.poll();

			if (player == null) {
				int id = playerCount.incrementAndGet();
				if (id > LibConfig.fakePlayerThreshold) Log.warn("Number of fake players in use has reached %d. Something may leak them", id);
				player = new OpenModsFakePlayer(world, id);
			}

			player.isDead = false;
			T result = user.usePlayer(player);
			player.setDead();
			pool.add(player);
			return result;
		}
	}

	private FakePlayerPool() {}

	public static final FakePlayerPool instance = new FakePlayerPool();

	private static final Map<World, WorldPool> worldPools = new WeakHashMap<World, WorldPool>();

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load evt) {
		worldPools.put(evt.world, new WorldPool());
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload evt) {
		worldPools.remove(evt.world);
	}

	public void executeOnPlayer(WorldServer world, PlayerUser user) {
		executeOnPlayer(world, wrap(user));
	}

	public <T> T executeOnPlayer(WorldServer world, PlayerUserReturning<T> user) {
		WorldPool pool = worldPools.get(world);
		if (pool != null) return pool.executeOnPlayer(world, user);
		else Log.warn("Trying to execute %s on world %s, but it's not loaded", user, world);
		return null;
	}
}
