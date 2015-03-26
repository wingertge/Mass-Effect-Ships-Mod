package com.octagon.airships.block.item;

import com.octagon.airships.reference.Config;
import com.octagon.airships.sync.SyncableEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import openmods.item.ItemOpenBlock;

import java.util.List;

public class ItemMachine extends ItemOpenBlock {
    public static final String ENERGY_STORAGE_TAG = "EnergyStorage";

    public ItemMachine(Block block) {
        super(block);
    }

    private static void addEnergyStorageInfo(NBTTagCompound tag, String name, String format, List<String> result) {
        if (tag.hasKey(name)) {
            SyncableEnergyStorage storage = new SyncableEnergyStorage(Config.EnergyStorage.MACHINE_GENERIC);
            storage.readFromNBT(tag, name);
            result.add(StatCollector.translateToLocalFormatted(format, storage.getEnergyStored(), Config.EnergyStorage.MACHINE_GENERIC));
        }
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addInformation(ItemStack stack, EntityPlayer player, List result, boolean extended) {
        NBTTagCompound tag = stack.stackTagCompound;
        if (tag != null) {
            addEnergyStorageInfo(tag, ENERGY_STORAGE_TAG, "masseffectships.misc.energyStorage", result);
        }
    }
}
