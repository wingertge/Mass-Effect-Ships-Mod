package openmods.utils.render;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import openmods.Mods;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class PaintUtils {

	private Set<Block> allowed;

	public static final PaintUtils instance = new PaintUtils();

	protected PaintUtils() {
		allowed = new HashSet<Block>();
		allowed.add(Blocks.stone);
		allowed.add(Blocks.cobblestone);
		allowed.add(Blocks.mossy_cobblestone);
		allowed.add(Blocks.sandstone);
		allowed.add(Blocks.iron_block);
		allowed.add(Blocks.stonebrick);
		allowed.add(Blocks.glass);
		allowed.add(Blocks.planks);
		allowed.add(Blocks.dirt);
		allowed.add(Blocks.log);
		allowed.add(Blocks.log2);
		allowed.add(Blocks.gold_block);
		allowed.add(Blocks.emerald_block);
		allowed.add(Blocks.lapis_block);
		allowed.add(Blocks.quartz_block);
		allowed.add(Blocks.end_stone);
		if (Loader.isModLoaded(Mods.TINKERSCONSTRUCT)) {
			addBlocksForMod(Mods.TINKERSCONSTRUCT, new String[] {
					"GlassBlock",
					"decoration.multibrick",
					"decoration.multibrickfancy"
			});
		}
		if (Loader.isModLoaded(Mods.EXTRAUTILITIES)) {
			addBlocksForMod(Mods.EXTRAUTILITIES, new String[] {
					"greenScreen",
					"extrautils:decor"
			});
		}
		if (Loader.isModLoaded(Mods.BIOMESOPLENTY)) {
			addBlocksForMod(Mods.BIOMESOPLENTY, new String[] {
					"bop.planks"
			});
		}
	}

	protected void addBlocksForMod(String modId, String[] blocks) {
		for (String blockName : blocks) {
			Block block = GameRegistry.findBlock(modId, blockName);
			if (block != null) {
				allowed.add(block);
			}
		}
	}

	public boolean isAllowedToReplace(Block block) {
		if (block == null || block.canProvidePower()) return false;
		return allowed.contains(block);
	}

	public boolean isAllowedToReplace(World world, int x, int y, int z) {
		if (world.isAirBlock(x, y, z)) { return false; }
		return isAllowedToReplace(world.getBlock(x, y, z));
	}
}
