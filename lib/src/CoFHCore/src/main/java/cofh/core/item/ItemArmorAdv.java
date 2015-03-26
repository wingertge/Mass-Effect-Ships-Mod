package cofh.core.item;

import cofh.lib.util.helpers.ItemHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemArmorAdv extends ItemArmor {

	public String repairIngot = "";
	public String[] textures = new String[2];
	protected Multimap<String, AttributeModifier> properties = HashMultimap.create();

	public ItemArmorAdv(ArmorMaterial material, int type) {

		super(material, 0, type);
	}

	public ItemArmorAdv setRepairIngot(String repairIngot) {

		this.repairIngot = repairIngot;
		return this;
	}

	public ItemArmorAdv setArmorTextures(String[] textures) {

		this.textures = textures;
		return this;
	}

	@Override
	public boolean getIsRepairable(ItemStack itemToRepair, ItemStack stack) {

		return ItemHelper.isOreNameEqual(stack, repairIngot);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {

		if (slot == 2) {
			return textures[1];
		}
		return textures[0];
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Multimap getAttributeModifiers(ItemStack stack) {

		Multimap<String, AttributeModifier> map = super.getAttributeModifiers(stack);
		map.putAll(properties);
		return map;
	}

	public ItemArmorAdv putAttribute(String attribute, AttributeModifier modifier) {

		properties.put(attribute, modifier);
		return this;
	}

	public Collection<AttributeModifier> removeAttribute(String attribute) {

		return properties.removeAll(attribute);
	}

}
