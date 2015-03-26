package com.octagon.airships.item;

import com.octagon.airships.reference.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemProbe extends AirshipsItem {
    public static enum Type {
        EMPTY("empty"),
        MINING("mining"),
        GAS("gas");

        public final String unlocalizedName;
        public final String iconName;

        private Type(String name) {
            this.unlocalizedName = name;
            this.iconName = Reference.MOD_ID + ":probe_" + name;
        }
    }

    private Type type;

    public ItemProbe(Type probeType) {
        type = probeType;
        this.setHasSubtypes(true);
    }

    @Override
    public void registerIcons(IIconRegister registry) {
        itemIcon = registry.registerIcon(type.iconName);
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return itemIcon;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getSubItems(Item item, CreativeTabs tab, List result) {
        result.add(new ItemStack(this));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        if(type != Type.EMPTY) list.add(StatCollector.translateToLocal(String.format("item.masseffectships.probe.%s.name", type.unlocalizedName)));
        else list.add(StatCollector.translateToLocal("masseffectships.misc.empty"));
    }
}
