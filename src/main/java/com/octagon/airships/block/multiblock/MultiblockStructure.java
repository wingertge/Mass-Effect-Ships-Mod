package com.octagon.airships.block.multiblock;

import com.google.common.collect.Sets;
import com.octagon.airships.sync.SyncableCoord;
import com.octagon.airships.sync.SyncableObjectBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

public class MultiblockStructure extends SyncableObjectBase<MultiblockStructure> {
    private Set<SyncableCoord> blocks = Sets.newHashSet();
    private SyncableCoord base;
    private boolean complete;

    public MultiblockStructure(SyncableCoord base) {
        this.base = base;
        addBlocks(base);
    }

    public Set<SyncableCoord> getBlocks() {
        return blocks;
    }

    public void setBlocks(Set<SyncableCoord> blocks) {
        this.blocks = blocks;
        markDirty();
    }

    @SuppressWarnings({"unchecked"})
    public void addBlocks(SyncableCoord... blocks) {
        this.blocks.addAll(Arrays.asList(blocks));
        markDirty();
    }

    @SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
    public void removeBlocks(SyncableCoord... blocks) {
        this.blocks.removeAll(Arrays.asList(blocks));
        markDirty();
    }

    public SyncableCoord getBase() {
        return base;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
        markDirty();
    }

    public void breakBlock(World world) {
        TileEntity tileEntity = world.getTileEntity(base.getX(), base.getY(), base.getZ());
        if(tileEntity != null && tileEntity instanceof TileEntityMultiblock) {
            TileEntityMultiblock tileEntityMultiblock = (TileEntityMultiblock)tileEntity;
            tileEntityMultiblock.rebuild();
        }
        markDirty();
    }

    public void clearBlocks() {
        blocks.clear();
        markDirty();
    }

    public void setBase(SyncableCoord base) {
        blocks.clear();
        this.base = base;
        blocks.add(base);
        markDirty();
    }

    @Override
    public void readFromStream(DataInputStream stream) throws IOException {
        int blocksLength = stream.readInt();
        complete = stream.readBoolean();

        base = new SyncableCoord();
        base.readFromStream(stream);

        blocks.clear();
        for(int i = 0; i < blocksLength; i++) {
            SyncableCoord block = new SyncableCoord();
            block.readFromStream(stream);
            blocks.add(block);
        }
    }

    @Override
    public void writeToStream(DataOutputStream stream) throws IOException {
        stream.writeInt(blocks.size());
        stream.writeBoolean(complete);
        base.writeToStream(stream);

        blocks.stream().forEach(b -> {
            try {
                b.writeToStream(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, String name) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("BlocksLength", blocks.size());
        tag.setBoolean("Complete", complete);
        base.writeToNBT(tag, "Base");

        NBTTagList blocksTag = new NBTTagList();
        blocks.stream().forEach(b -> {
            NBTTagCompound tagCompound = new NBTTagCompound();
            b.writeToNBT(tagCompound, "Coord");
            blocksTag.appendTag(tagCompound);
        });

        tag.setTag("Blocks", blocksTag);

        nbt.setTag(name, tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt, String name) {
        NBTTagCompound tag = nbt.getCompoundTag(name);

        int blocksLength = tag.getInteger("BlocksLength");
        complete = tag.getBoolean("Complete");
        base = new SyncableCoord();
        base.readFromNBT(tag, "Base");

        blocks.clear();
        NBTTagList blocksTag = tag.getTagList("Blocks", 10);
        for(int i = 0; i < blocksLength; i++) {
            SyncableCoord block = new SyncableCoord();
            block.readFromNBT(blocksTag.getCompoundTagAt(i), "Coord");
            blocks.add(block);
        }
    }

    public void assign(MultiblockStructure structure) {
        blocks = structure.blocks;
        base = structure.base;
        complete = structure.complete;
        markDirty();
    }
}
