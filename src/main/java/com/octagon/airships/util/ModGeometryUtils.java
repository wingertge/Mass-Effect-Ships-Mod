package com.octagon.airships.util;

import com.google.common.collect.Sets;
import openmods.utils.Coord;
import openmods.utils.render.GeometryUtils;

import java.util.Set;

public class ModGeometryUtils {
    public static Set<Coord> makeSphereFilling(int radiusX, int radiusY, int radiusZ) {
        final double invRadiusX = 1.0 / (radiusX + 0.5);
        final double invRadiusY = 1.0 / (radiusY + 0.5);
        final double invRadiusZ = 1.0 / (radiusZ + 0.5);
        Set<Coord> sphere = Sets.newHashSet();

        final Set<GeometryUtils.Octant> octantSet = GeometryUtils.Octant.ALL;

        double nextXn = 0;
        forX: for (int x = 0; x <= radiusX; ++x) {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadiusX;
            double nextYn = 0;
            forY: for (int y = 0; y <= radiusY; ++y) {
                final double yn = nextYn;
                nextYn = (y + 1) * invRadiusY;
                double nextZn = 0;
                forZ: for (int z = 0; z <= radiusZ; ++z) {
                    final double zn = nextZn;
                    nextZn = (z + 1) * invRadiusZ;

                    double distanceSq = MathUtils.lengthSq(xn, yn, zn);
                    if (distanceSq > 1) {
                        if (z == 0) {
                            if (y == 0) {
                                break forX;
                            }
                            break forY;
                        }
                        break forZ;
                    }

                    if (MathUtils.lengthSq(nextXn, yn, zn) <= 1
                            && MathUtils.lengthSq(xn, nextYn, zn) <= 1
                            && MathUtils.lengthSq(xn, yn, nextZn) <= 1) {
                        for (GeometryUtils.Octant octant : octantSet) {
                            sphere.add(new Coord(x * octant.getXOffset(), y
                                    * octant.getYOffset(), z * octant.getZOffset()));
                        }
                    }
                }
            }
        }

        return sphere;
    }
}
