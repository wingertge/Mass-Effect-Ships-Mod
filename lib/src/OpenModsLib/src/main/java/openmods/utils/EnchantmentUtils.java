package openmods.utils;

import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EnchantmentUtils {

	public static final int XP_PER_BOTTLE = 8;
	public static final int RATIO = 20;
	public static final int LIQUID_PER_XP_BOTTLE = XP_PER_BOTTLE * RATIO;

	public static int calcEnchantability(ItemStack itemStack, int power, boolean max) {
		Item item = itemStack.getItem();
		int k = item.getItemEnchantability();
		return calcEnchantability(k, power, max);
	}

	public static int calcEnchantability(int enchantability, int power, boolean max) {
		if (enchantability <= 0) { return 0; }
		if (power > 15) {
			power = 15;
		}

		int l = (max? 7 : 0) + 1 + (power >> 1) + (max? power : 0);
		return max? Math.max(l, power * 2) : Math.max(l / 3, 1);
	}

	/**
	 * Be warned, minecraft doesn't update experienceTotal properly, so we have
	 * to do this.
	 *
	 * @param player
	 * @return
	 */
	public static int getPlayerXP(EntityPlayer player) {
		return (int)(EnchantmentUtils.getExperienceForLevel(player.experienceLevel) + (player.experience * player.xpBarCap()));
	}

	public static void addPlayerXP(EntityPlayer player, int amount) {
		int experience = getPlayerXP(player) + amount;
		player.experienceTotal = experience;
		player.experienceLevel = EnchantmentUtils.getLevelForExperience(experience);
		int expForLevel = EnchantmentUtils.getExperienceForLevel(player.experienceLevel);
		player.experience = (float)(experience - expForLevel) / (float)player.xpBarCap();
	}

	public static boolean enchantItem(ItemStack itemstack, int level, Random rand) {
		if (itemstack == null) return false;

		@SuppressWarnings("unchecked")
		List<EnchantmentData> enchantments = EnchantmentHelper.buildEnchantmentList(rand, itemstack, level);
		if (enchantments == null || enchantments.isEmpty()) return false;

		boolean isBook = itemstack.getItem() == Items.book;

		if (isBook) {
			itemstack.func_150996_a(Items.enchanted_book);

			final int count = enchantments.size();
			int ignored = count > 1? rand.nextInt(count) : -1;
			for (int i = 0; i < count; i++)
				if (i != ignored) Items.enchanted_book.addEnchantment(itemstack, enchantments.get(i));
		} else {
			for (EnchantmentData enchantment : enchantments)
				itemstack.addEnchantment(enchantment.enchantmentobj, enchantment.enchantmentLevel);
		}

		return true;
	}

	public static int getExperienceForLevel(int level) {
		if (level == 0) { return 0; }
		if (level > 0 && level < 16) {
			return level * 17;
		} else if (level > 15 && level < 31) {
			return (int)(1.5 * Math.pow(level, 2) - 29.5 * level + 360);
		} else {
			return (int)(3.5 * Math.pow(level, 2) - 151.5 * level + 2220);
		}
	}

	public static int getXpToNextLevel(int level) {
		int levelXP = EnchantmentUtils.getLevelForExperience(level);
		int nextXP = EnchantmentUtils.getExperienceForLevel(level + 1);
		return nextXP - levelXP;
	}

	public static int getLevelForExperience(int experience) {
		int i = 0;
		while (getExperienceForLevel(i) <= experience) {
			i++;
		}
		return i - 1;
	}

	public static float getPower(World worldObj, int xCoord, int yCoord, int zCoord) {
		float power = 0;

		for (int x = -1; x <= 1; ++x) {
			for (int z = -1; z <= 1; ++z) {
				final int innerX = xCoord + z;
				final int innerZ = zCoord + x;
				final int outerX = xCoord + z * 2;
				final int outerZ = zCoord + x * 2;
				final int middle = yCoord;
				final int top = yCoord + 1;
				if ((x != 0 || z != 0)
						&& worldObj.isAirBlock(innerX, middle, innerZ)
						&& worldObj.isAirBlock(innerX, top, innerZ)) {

					power += ForgeHooks.getEnchantPower(worldObj, outerX, middle, outerZ);
					power += ForgeHooks.getEnchantPower(worldObj, outerX, top, outerZ);

					if (z != 0 && x != 0) {
						power += ForgeHooks.getEnchantPower(worldObj, outerX, middle, innerZ);
						power += ForgeHooks.getEnchantPower(worldObj, outerX, top, innerZ);
						power += ForgeHooks.getEnchantPower(worldObj, innerX, middle, outerZ);
						power += ForgeHooks.getEnchantPower(worldObj, innerX, top, outerZ);
					}
				}
			}
		}
		return power;
	}

	public static int liquidToXPRatio(int liquid) {
		return liquid / RATIO;
	}

	public static int XPToLiquidRatio(int xp) {
		return xp * RATIO;
	}

	public static int getLiquidForLevel(int level) {
		return XPToLiquidRatio(getExperienceForLevel(level));
	}

	public static void addAllBooks(Enchantment enchantment, List<ItemStack> items) {
		for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); i++)
			items.add(Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, i)));
	}
}