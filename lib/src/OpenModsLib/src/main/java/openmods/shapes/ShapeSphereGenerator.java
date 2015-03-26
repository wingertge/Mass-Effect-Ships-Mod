package openmods.shapes;

import openmods.utils.render.GeometryUtils;
import openmods.utils.render.GeometryUtils.Octant;

public class ShapeSphereGenerator implements IShapeGenerator {

	@Override
	public void generateShape(int radiusX, int radiusY, int radiusZ, IShapeable shapeable) {
		GeometryUtils.makeSphere(radiusX, radiusY, radiusZ, shapeable, Octant.ALL);
	}
}
