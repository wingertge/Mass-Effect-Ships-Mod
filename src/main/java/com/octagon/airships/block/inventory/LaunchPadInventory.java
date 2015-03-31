package com.octagon.airships.block.inventory;

import com.octagon.airships.block.tileentity.TileEntityLaunchPad;
import com.octagon.airships.block.tileentity.TileEntityLaunchPadIO;
import com.octagon.airships.block.tileentity.TileEntityLaunchPadMultiblock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

public class LaunchPadInventory implements IInventory {
    private List<TileEntityLaunchPad> launchPads;
    private TileEntityLaunchPadMultiblock multiblock;
    private TileEntityLaunchPadIO io;

    public LaunchPadInventory(List<TileEntityLaunchPad> launchPads, TileEntityLaunchPadMultiblock multiblock) {
        this.launchPads = launchPads;
        this.multiblock = multiblock;
    }

    @Override
    public int getSizeInventory() {
        return launchPads.size() + multiblock.getCoreInventory().getSizeInventory() + io.getInventory().getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if(slot < multiblock.getCoreInventory().getSizeInventory()) return multiblock.getCoreInventory().getStackInSlot(slot);
        if(slot < multiblock.getCoreInventory().getSizeInventory() + io.getInventory().getSizeInventory())
            return io.getInventory().getStackInSlot(slot - multiblock.getCoreInventory().getSizeInventory());
        return launchPads.get(slot - multiblock.getCoreInventory().getSizeInventory() - io.getInventory().getSizeInventory()).getInventory().getStackInSlot(0);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if(slot < multiblock.getCoreInventory().getSizeInventory()) return multiblock.getCoreInventory().decrStackSize(slot, amount);
        if(slot < multiblock.getCoreInventory().getSizeInventory() + io.getInventory().getSizeInventory())
            return io.getInventory().decrStackSize(slot - multiblock.getCoreInventory().getSizeInventory(), amount);
        return launchPads.get(slot - multiblock.getCoreInventory().getSizeInventory() - io.getInventory().getSizeInventory()).getInventory().decrStackSize(0, amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return getStackInSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if(slot < multiblock.getCoreInventory().getSizeInventory()) multiblock.getCoreInventory().setInventorySlotContents(slot, stack);
        else if(slot < multiblock.getCoreInventory().getSizeInventory() + io.getInventory().getSizeInventory())
            io.getInventory().setInventorySlotContents(slot - multiblock.getCoreInventory().getSizeInventory(), stack);
        else launchPads.get(slot - multiblock.getCoreInventory().getSizeInventory() - io.getInventory().getSizeInventory()).getInventory().setInventorySlotContents(0, stack);
    }

    @Override
    public String getInventoryName() {
        return "Launch Pad";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if(slot < multiblock.getCoreInventory().getSizeInventory()) return multiblock.getCoreInventory().isItemValidForSlot(slot, stack);
        if(slot < multiblock.getCoreInventory().getSizeInventory() + io.getInventory().getSizeInventory())
            return io.getInventory().isItemValidForSlot(slot - multiblock.getCoreInventory().getSizeInventory(), stack);
        return launchPads.get(slot - multiblock.getCoreInventory().getSizeInventory() - io.getInventory().getSizeInventory()).getInventory().isItemValidForSlot(0, stack);
    }

    public void setLaunchPads(List<TileEntityLaunchPad> launchPads) {
        this.launchPads = launchPads;
    }

    public void setIOBlock(TileEntityLaunchPadIO io) { this.io = io; }
}
