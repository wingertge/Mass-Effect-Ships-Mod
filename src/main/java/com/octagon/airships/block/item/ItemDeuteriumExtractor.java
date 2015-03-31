package com.octagon.airships.block.item;

import com.octagon.airships.reference.Config;
import com.octagon.airships.sync.MonitoredTank;
import com.octagon.airships.util.KeyHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemDeuteriumExtractor extends ItemMachine {
    public static final String INPUT_TANK_TAG = "InputTank";
    public static final String OUTPUT_TANK_TAG = "OutputTank";

    public ItemDeuteriumExtractor(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List result, boolean extended) {
        super.addInformation(stack, player, result, extended);

        boolean shiftDown = KeyHelper.isShiftKeyDown();
        if(stack.stackTagCompound != null) {
            if(shiftDown)
                addTankInformation(stack.stackTagCompound, result);
            else
                result.add(StatCollector.translateToLocal("masseffectships.misc.extendTooltip"));
        }
    }

    private static void addTankInformation(NBTTagCompound tag, List<String> result) {
        if (tag.hasKey(INPUT_TANK_TAG)) {
            MonitoredTank inputTank = new MonitoredTank(Config.FluidStorage.DEUTERIUM_EXTRACTOR_IN);

            inputTank.readFromNBT(tag, INPUT_TANK_TAG);
            if(inputTank.getFluid() != null)
                result.add(StatCollector.translateToLocalFormatted("masseffectships.misc.fluidStorage", inputTank.getFluid().getLocalizedName(), inputTank.getFluidAmount(), inputTank.getCapacity()));
        }
        if(tag.hasKey(OUTPUT_TANK_TAG)) {
            MonitoredTank outputTank = new MonitoredTank(Config.FluidStorage.DEUTERIUM_EXTRACTOR_OUT);
            outputTank.readFromNBT(tag, OUTPUT_TANK_TAG);
            if(outputTank.getFluid() != null)
                result.add(StatCollector.translateToLocalFormatted("masseffectships.misc.fluidStorage", outputTank.getFluid().getLocalizedName(), outputTank.getFluidAmount(), outputTank.getCapacity()));
        }
    }
}
