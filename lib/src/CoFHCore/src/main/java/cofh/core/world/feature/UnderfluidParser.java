package cofh.core.world.feature;

import cofh.core.world.FeatureParser;
import cofh.lib.util.WeightedRandomBlock;
import cofh.lib.world.feature.FeatureBase;
import cofh.lib.world.feature.FeatureBase.GenRestriction;
import cofh.lib.world.feature.FeatureGenUnderfluid;
import com.google.gson.JsonObject;

import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.DungeonHooks.DungeonMob;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.apache.logging.log4j.Logger;

public class UnderfluidParser extends UniformParser {

	private boolean isUnderwater;

	public UnderfluidParser(boolean water) {

		isUnderwater = water;
	}

	@Override
	protected List<WeightedRandomBlock> generateDefaultMaterial() {

		return Arrays.asList(new WeightedRandomBlock(Blocks.dirt, -1), new WeightedRandomBlock(Blocks.grass, -1));
	}

	@Override
	protected FeatureBase getFeature(String featureName, JsonObject genObject, WorldGenerator gen, List<WeightedRandomBlock> matList, int numClusters, GenRestriction biomeRes, boolean retrogen,
			GenRestriction dimRes, Logger log) {

		boolean water = true;
		int[] fluidList = null;
		l: if (genObject.has("genFluid")) {
			ArrayList<DungeonMob> list = new ArrayList<DungeonMob>();
			if (!FeatureParser.parseWeightedStringList(genObject.get("genFluid"), list)) {
				break l;
			}
			water = false;
			TIntHashSet ints = new TIntHashSet();
			for (DungeonMob str : list) {
				//ints.add(FluidRegistry.getFluidID(str.type));
				// NOPE. this NPEs.
				Fluid fluid = FluidRegistry.getFluid(str.type);
				if (fluid != null)
					ints.add(fluid.getID());
			}
			fluidList = ints.toArray();
		}
		if (water)
			return new FeatureGenUnderfluid(featureName, gen, matList, numClusters, biomeRes, retrogen, dimRes);
		else
			return new FeatureGenUnderfluid(featureName, gen, matList, fluidList, numClusters, biomeRes, retrogen, dimRes);
	}

	@Override
	protected String getDefaultTemplate() {

		return isUnderwater ? "plate" : super.getDefaultTemplate();
	}

}
