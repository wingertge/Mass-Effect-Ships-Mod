package com.octagon.airships.block.item;

import com.octagon.airships.reference.Config;
import com.octagon.airships.sync.MonitoredTank;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import openmods.item.ItemOpenBlock;

import java.util.List;

public class ItemLaunchPad extends ItemOpenBlock {
    public static final String TANK_1_TAG = "Tank1";
    public static final String TANK_2_TAG = "Tank2";
    public static final String CURRENT_WORK_TAG = "CurrentWork";

    public ItemLaunchPad(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List result, boolean extended) {
        NBTTagCompound tag = stack.stackTagCompound;

        if (tag != null) {
            addTankInfo(tag, TANK_1_TAG, "masseffectships.misc.fluidStorage", result);
            addTankInfo(tag, TANK_2_TAG, "masseffectships.misc.fluidStorage", result);
        }
    }

    private void addTankInfo(NBTTagCompound tag, String name, String pattern, List result) {
        if(tag.hasKey(name)) {
            MonitoredTank tank = new MonitoredTank(Config.FluidStorage.LAUNCH_PAD);
            tank.readFromNBT(tag, name);
            result.add(StatCollector.translateToLocalFormatted(pattern,
                    tank.getFluid() != null ? tank.getFluid().getLocalizedName() : StatCollector.translateToLocal("masseffectships.misc.empty"),
                    tank.getFluidAmount(), Config.FluidStorage.LAUNCH_PAD));
        }
    }
}
