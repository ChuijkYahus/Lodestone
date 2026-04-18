package team.lodestar.lodestone.modules.rendering.particle.runtime.profile.sphere;

import net.minecraft.util.RandomSource;
import team.lodestar.lodestone.modules.core.easing.Easing;

public class SphereInfo {

    public enum PlacementMode {
        DISTRIBUTE,
        OUTLINE
    }

    private static final RandomSource SPHERE_RANDOM = RandomSource.create();

    private final double[] min;
    private final double[] max;

    private PlacementMode mode = PlacementMode.DISTRIBUTE;
    private Easing angleWeight = Easing.LINEAR;
    private int angleSections = 1;
    private Easing distanceWeight = Easing.LINEAR;

    public SphereInfo(double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
        this.min = new double[]{xMin, yMin, zMin};
        this.max = new double[]{xMax, yMax, zMax};
    }

    public SphereInfo weighedAngle(Easing angleWeight, int angleSections) {
        this.angleWeight = angleWeight;
        this.angleSections = angleSections;
        return this;
    }

    public SphereInfo weighedDistance(Easing distanceWeight) {
        this.distanceWeight = distanceWeight;
        return this;
    }

    public SphereInfo mode(PlacementMode mode) {
        this.mode = mode;
        return this;
    }

    public double[] pickPosition() {
        double minAngle = 0;
        double maxAngle = 6.28f;
        if (angleSections > 1) {
            double sectionSize = 6.28f / angleSections;
            double sector = SPHERE_RANDOM.nextInt(angleSections) * sectionSize;
            double half = sectionSize / 2;
            minAngle = sector - half;
            maxAngle = sector + half;
        }
        double angle = angleWeight.lerp(SPHERE_RANDOM.nextFloat(), minAngle, maxAngle);
        double x = Math.sin(angle);
        double y = SPHERE_RANDOM.nextDouble();
        double z = Math.cos(angle);
        double width = distanceWeight.asValueDistribution(y, 0, 1, 0);
        switch (mode) {
            case DISTRIBUTE -> {
                x *= distanceWeight.lerp(SPHERE_RANDOM.nextDouble(), min[0], max[0]) * width;
                y = distanceWeight.lerp(y, min[1], max[1]);
                z *= distanceWeight.lerp(SPHERE_RANDOM.nextDouble(), min[2], max[2]) * width;
            }
            case OUTLINE -> {
                x = distanceWeight.lerp(x, min[0], max[0]) * width;
                y = distanceWeight.lerp(y, min[1], max[1]);
                z = distanceWeight.lerp(z, min[2], max[2]) * width;
            }
        }
        return new double[]{x, y, z};
    }
}
