package com.octagon.airships.block.tileentity;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.octagon.airships.block.multiblock.MultiblockStructure;
import com.octagon.airships.block.multiblock.TileEntityMultiblock;
import com.octagon.airships.client.gui.machine.ContainerEezoCoreBase;
import com.octagon.airships.client.gui.machine.GuiEezoCoreBase;
import com.octagon.airships.init.ModBlocks;
import com.octagon.airships.reference.Config;
import com.octagon.airships.sync.SyncableCoord;
import com.octagon.airships.sync.rpc.IRadiusChanger;
import com.octagon.airships.util.ModGeometryUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.api.IActivateAwareTile;
import openmods.api.IBreakAwareTile;
import openmods.api.IHasGui;
import openmods.api.IPlaceAwareTile;
import openmods.shapes.IShapeable;
import openmods.sync.ISyncListener;
import openmods.sync.ISyncableObject;
import openmods.sync.SyncableBoolean;
import openmods.sync.SyncableInt;
import openmods.utils.BlockNotifyFlags;
import openmods.utils.Coord;
import openmods.utils.render.GeometryUtils;

import java.util.Set;

public class TileEntityEezoCoreMultiblock extends TileEntityMultiblock implements IShapeable, IActivateAwareTile, ISyncListener, IHasGui, IRadiusChanger, IBreakAwareTile, IPlaceAwareTile {

    private Set<Coord> guideShape;
    private SyncableInt radius;
    private SyncableInt color;
    private SyncableBoolean guideActive;
    private float timeSinceChange;
    private int lastRadius = 0;
    private MultiblockStructure structure;
    private int updatePending = 0;

    public TileEntityEezoCoreMultiblock() {
        syncMap.addUpdateListener(this);
        updatePending = 2;
    }

    @Override
    protected void createSyncedFields() {
        radius = new SyncableInt(2);
        color = new SyncableInt(0xFFFFFF);
        guideActive = new SyncableBoolean(true);
        structure = new MultiblockStructure(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));
    }

    private void recreateShape() {
        guideShape = Sets.newHashSet();
        GeometryUtils.makeSphere(getRadius(), getRadius(), getRadius(), this, GeometryUtils.Octant.ALL);
        validateStructure();
    }

    @Override
    public SyncableCoord getBaseBlock() {
        return structure.getBase();
    }

    @Override
    public boolean isComplete() {
        return structure != null && structure.isComplete();
    }

    @Override
    public void rebuild() {
        removeMultiblock();
    }

    public boolean shouldRenderGuide() {
        return guideActive.get();
    }

    @Override
    public void setBlock(int x, int y, int z) {
        guideShape.add(new Coord(x, y, z));
    }

    public Set<Coord> getShape() {
        return guideShape;
    }

    @Override
    public void onSync(Set<ISyncableObject> changes) {
        if (changes.contains(radius)) {
            if(lastRadius != radius.get()) {
                lastRadius = radius.get();
                recreateShape();
                timeSinceChange = 0;
            }
        }
    }

    public float getTimeSinceChange() {
        return timeSinceChange;
    }

    public int getColor() {
        return color.get() & 0x00FFFFFF;
    }

    public int getRadius() {
        return radius.get();
    }

    public int getCount() {
        if (guideShape == null) recreateShape();
        return guideShape.size();
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) {
            if (timeSinceChange < 1.0) {
                timeSinceChange = (float)Math.min(1.0f, timeSinceChange + 0.1);
            }
        }

        if(updatePending > 0) {
            updatePending--;
            if(updatePending == 0) {
                recreateShape();
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        AxisAlignedBB box = super.getRenderBoundingBox();
        return box.expand(getRadius(), getRadius(), getRadius());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return Config.Sizes.EEZO_CORE_RENDER_DISTANCE_SQ;
    }

    private static void inc(SyncableInt v) {
        v.modify(+1);
    }

    private static void dec(SyncableInt v) {
        if (v.get() > 0) v.modify(-1);
    }

    @Override
    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(player.isSneaking()) recreateShape();

        return true;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    public void setRadius(int radius) {
        Preconditions.checkArgument(radius > 0, "Radius must be > 0");
        Preconditions.checkArgument(radius <= Config.Sizes.MAX_CORE_RADIUS, "Radius must be <= " + Config.Sizes.MAX_CORE_RADIUS);

        this.radius.set(radius);

        recreateShape();
        if(!worldObj.isRemote) sync();
    }

    public void validateStructure() {
        Set<SyncableCoord> hullBlocks = Sets.newHashSet();

        for(Coord coord : guideShape) {
            int x, y, z;
            x = xCoord + coord.x;
            y = yCoord + coord.y + getRadius();
            z = zCoord + coord.z;
            Block block = worldObj.getBlock(x, y, z);
            if(block.equals(ModBlocks.eezoCoreHull)) {
                hullBlocks.add(new SyncableCoord(worldObj, x, y, z));
                TileEntityMultiblockPart tileEntity = (TileEntityMultiblockPart)worldObj.getTileEntity(x, y, z);
                tileEntity.setBase(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));
            }
        }


        if(hullBlocks.size() == getShape().size() - 1) {
            Set<Coord> coords = ModGeometryUtils.makeSphereFilling(getRadius(), getRadius(), getRadius());
            Set<SyncableCoord> coreBlocks = Sets.newHashSet();
            for(Coord coord : coords) {
                int x, y, z;
                x = xCoord + coord.x;
                y = yCoord + coord.y + getRadius();
                z = zCoord + coord.z;
                Block block = worldObj.getBlock(x, y, z);
                if(block.equals(ModBlocks.refinedEezoBlock)) coreBlocks.add(new SyncableCoord(worldObj, x, y, z));
            }

            if(coreBlocks.size() == coords.size()) {
                completeMultiblock(hullBlocks, coreBlocks);
                return;
            }
        }

        removeMultiblock();
    }

    private void removeMultiblock() {
        if(structure == null) return;

        for(SyncableCoord instance : structure.getBlocks()) {
            TileEntity tileEntity = worldObj.getTileEntity(instance.getX(), instance.getY(), instance.getZ());
            if(tileEntity != null && tileEntity instanceof TileEntityMultiblockPart) {
                TileEntityMultiblockPart part = (TileEntityMultiblockPart)tileEntity;
                part.setBase(new SyncableCoord());
                part.setStructure(new MultiblockStructure(new SyncableCoord()));
                worldObj.setBlockMetadataWithNotify(instance.getX(), instance.getY(), instance.getZ(), 0, BlockNotifyFlags.SEND_TO_CLIENTS);
                worldObj.markBlockForUpdate(instance.getX(), instance.getY(), instance.getZ());
            }
        }

        structure.setComplete(false);
        structure.clearBlocks();
        structure.setBase(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));

        if(!worldObj.isRemote) sync();
    }

    private void completeMultiblock(Set<SyncableCoord> hullBlocks, Set<SyncableCoord> coreBlocks) {
        structure.assign(new MultiblockStructure(new SyncableCoord(worldObj, xCoord, yCoord, zCoord)));
        structure.clearBlocks();
        Set<SyncableCoord> blocks = Sets.newHashSet();
        blocks.addAll(hullBlocks);
        blocks.addAll(coreBlocks);
        structure.setBlocks(blocks);
        structure.setComplete(true);

        for(SyncableCoord instance : structure.getBlocks()) {
            TileEntity tileEntity = worldObj.getTileEntity(instance.getX(), instance.getY(), instance.getZ());
            if(tileEntity != null && tileEntity instanceof TileEntityMultiblockPart) {
                TileEntityMultiblockPart part = (TileEntityMultiblockPart)tileEntity;
                part.setBase(new SyncableCoord(worldObj, xCoord, yCoord, zCoord));
                part.setStructure(structure);
                worldObj.setBlockMetadataWithNotify(instance.getX(), instance.getY(), instance.getZ(), 1, BlockNotifyFlags.ALL);
                worldObj.markBlockForUpdate(instance.getX(), instance.getY(), instance.getZ());
            }
        }

        if(!worldObj.isRemote) sync();
    }

    @Override
    public Object getServerGui(EntityPlayer player) {
        return new ContainerEezoCoreBase(player.inventory, this);
    }

    @Override
    public Object getClientGui(EntityPlayer player) {
        return new GuiEezoCoreBase(new ContainerEezoCoreBase(player.inventory, this));
    }

    @Override
    public boolean canOpenGui(EntityPlayer player) {
        return true;
    }

    @Override
    public void changeRadius(int radius) {
        if (!worldObj.isRemote) {
            setRadius(radius);
        }
    }

    public IRadiusChanger createRpcProxy() {
        return createClientRpcProxy(IRadiusChanger.class);
    }

    public boolean isActive() {
        return false;
    }

    @Override
    public void onBlockBroken() {
        removeMultiblock();
    }

    @Override
    public void setStructure(MultiblockStructure structure) { this.structure = structure; }

    @Override
    public MultiblockStructure getStructure() {
        return structure;
    }

    @Override
    public void queueStructureValidation() {
        updatePending = 1;
    }

    @Override
    public void onBlockPlacedBy(EntityPlayer player, ForgeDirection side, ItemStack stack, float hitX, float hitY, float hitZ) {
        for(int i = 1; i <= Config.Sizes.MAX_CORE_RADIUS; i++) {
            setRadius(i);
            if(structure.isComplete()) break;
        }
        if(!structure.isComplete()) setRadius(2);

        super.onBlockPlacedBy(player, side, stack, hitX, hitY, hitZ);
    }
}
