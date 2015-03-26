package openmods.liquids;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class GenericFluidHandler implements IFluidHandler {

	public static class Source extends GenericFluidHandler {
		public Source(IFluidTank tank) {
			super(tank);
		}

		@Override
		public boolean canFill(ForgeDirection from, Fluid fluid) {
			return false;
		}

		@Override
		public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
			return 0;
		}
	}

	public static class Drain extends GenericFluidHandler {
		public Drain(IFluidTank tank) {
			super(tank);
		}

		@Override
		public boolean canDrain(ForgeDirection from, Fluid fluid) {
			return false;
		}

		@Override
		public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
			return null;
		}

		@Override
		public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
			return null;
		}
	}

	private final IFluidTank tank;

	public GenericFluidHandler(IFluidTank tank) {
		this.tank = tank;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (resource == null || !canFill(from, resource.getFluid())) return 0;
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if (resource == null || !resource.isFluidEqual(tank.getFluid()) || !canDrain(from, resource.getFluid())) { return null; }
		return tank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if (!canDrain(from, null)) return null;
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { tank.getInfo() };
	}

}
