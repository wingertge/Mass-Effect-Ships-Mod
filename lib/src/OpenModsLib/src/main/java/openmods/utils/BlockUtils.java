package openmods.utils;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.inventory.IInventoryProvider;

public class BlockUtils {

	public static final ForgeDirection DEFAULT_BLOCK_DIRECTION = ForgeDirection.WEST;

	public static ForgeDirection get2dOrientation(EntityLivingBase entity) {
		int l = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;
		switch (l) {
			case 0:
				return ForgeDirection.SOUTH;
			case 1:
				return ForgeDirection.WEST;
			case 2:
				return ForgeDirection.NORTH;
			case 3:
				return ForgeDirection.EAST;
		}
		return ForgeDirection.SOUTH;

	}

	public static float getRotationFromDirection(ForgeDirection direction) {
		switch (direction) {
			case NORTH:
				return 0F;
			case SOUTH:
				return 180F;
			case WEST:
				return 90F;
			case EAST:
				return -90F;
			case DOWN:
				return -90f;
			case UP:
				return 90f;
			default:
				return 0f;
		}
	}

	public static ForgeDirection get3dOrientation(EntityLivingBase entity) {
		if (entity.rotationPitch > 45.5F) {
			return ForgeDirection.DOWN;
		} else if (entity.rotationPitch < -45.5F) { return ForgeDirection.UP; }
		return get2dOrientation(entity);
	}

	public static EntityItem dropItemStackInWorld(World worldObj, double x, double y, double z, ItemStack stack) {
		float f = 0.7F;
		float d0 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		float d1 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		float d2 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		EntityItem entityitem = new EntityItem(worldObj, x + d0, y + d1, z + d2, stack);
		entityitem.delayBeforeCanPickup = 10;
		if (stack.hasTagCompound()) {
			entityitem.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
		}
		worldObj.spawnEntityInWorld(entityitem);
		return entityitem;
	}

	public static EntityItem ejectItemInDirection(World world, double x, double y, double z, ForgeDirection direction, ItemStack stack) {
		EntityItem item = BlockUtils.dropItemStackInWorld(world, x, y, z, stack);
		item.motionX = direction.offsetX / 5F;
		item.motionY = direction.offsetY / 5F;
		item.motionZ = direction.offsetZ / 5F;
		return item;
	}

	public static boolean getTileInventoryDrops(TileEntity tileEntity, List<ItemStack> drops) {
		if (tileEntity == null) return false;

		if (tileEntity instanceof IInventory) {
			drops.addAll(InventoryUtils.getInventoryContents((IInventory)tileEntity));
			return true;
		} else if (tileEntity instanceof IInventoryProvider) {
			drops.addAll(InventoryUtils.getInventoryContents(((IInventoryProvider)tileEntity).getInventory()));
			return true;
		}

		return false;
	}

	public static void dropInventory(IInventory inventory, World world, double x, double y, double z) {
		if (inventory == null) { return; }
		for (int i = 0; i < inventory.getSizeInventory(); ++i) {
			ItemStack itemStack = inventory.getStackInSlot(i);
			if (itemStack != null) {
				dropItemStackInWorld(world, x, y, z, itemStack);
			}
		}
	}

	public static void dropInventory(IInventory inventory, World world, int x, int y, int z) {
		dropInventory(inventory, world, x + 0.5, y + 0.5, z + 0.5);
	}

	public static TileEntity getTileInDirection(TileEntity tile, ForgeDirection direction) {
		int targetX = tile.xCoord + direction.offsetX;
		int targetY = tile.yCoord + direction.offsetY;
		int targetZ = tile.zCoord + direction.offsetZ;
		return tile.getWorldObj().getTileEntity(targetX, targetY, targetZ);
	}

	public static TileEntity getTileInDirection(World world, Coord coord, ForgeDirection direction) {
		int targetX = coord.x + direction.offsetX;
		int targetY = coord.y + direction.offsetY;
		int targetZ = coord.z + direction.offsetZ;
		return world.getTileEntity(targetX, targetY, targetZ);
	}

	public static int getFirstNonAirBlockFromTop(World world, int x, int z) {
		int y;
		for (y = world.getActualHeight(); world.isAirBlock(x, y - 1, z) && y > 0; y--) {}
		return y;
	}

	public static boolean isBlockHit(MovingObjectPosition mop, TileEntity tile) {
		if (tile == null) return false;
		return isBlockHit(mop, tile.xCoord, tile.yCoord, tile.zCoord);
	}

	public static boolean isBlockHit(MovingObjectPosition mop, int x, int y, int z) {
		if (mop == null) return false;
		return mop.blockX == x && mop.blockY == y && mop.blockZ == z;
	}

}
