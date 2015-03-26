package openmods.shapes;

import openmods.utils.render.GeometryUtils;
import openmods.utils.render.GeometryUtils.Octant;

public class ShapeDomeGenerator implements IShapeGenerator {

	@Override
	public void generateShape(int xSize, int ySize, int zSize, IShapeable shapeable) {
		GeometryUtils.makeSphere(xSize, ySize, zSize, shapeable, Octant.TOP);
	}

}
