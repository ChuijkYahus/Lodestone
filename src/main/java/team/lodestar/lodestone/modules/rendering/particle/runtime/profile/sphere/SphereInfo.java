package team.lodestar.lodestone.modules.rendering.particle.runtime.profile.sphere;

import net.minecraft.util.Mth;
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

    public SphereInfo weighedAngle(Easing angleWeight) {
        this.angleWeight = angleWeight;
        return this;
    }

    public SphereInfo sectioned(int angleSections) {
        this.angleSections = angleSections;
        return this;
    }

    public SphereInfo weighedDistance(Easing distanceWeight) {
        this.distanceWeight = Easing.SINE_IN_OUT;
        return this;
    }

    public SphereInfo mode(PlacementMode mode) {
        this.mode = mode;
        return this;
    }

    public double[] pickPosition() {
        double minAngle = 0;
        double maxAngle = 6.28;

        if (angleSections > 1) {
            double sectionSize = 6.28 / angleSections;
            double sector = SPHERE_RANDOM.nextInt(angleSections) * sectionSize;
            double half = sectionSize / 2.0;
            minAngle = sector - half;
            maxAngle = sector + half;
        }

        double angleDelta = SPHERE_RANDOM.nextDouble();
        double angle = angleWeight.asValueDistribution(
                angleDelta,
                minAngle,
                maxAngle
        );



        double pitch = SPHERE_RANDOM.nextDouble() * 6.28 - (3.14);
        double cosPitch = Math.cos(pitch);

        double x = Math.cos(angle) * cosPitch;
        double y = Math.sin(pitch);
        double z = Math.sin(angle) * cosPitch;

        x = (x + 1.0) * 0.5;
        y = (y + 1.0) * 0.5;
        z = (z + 1.0) * 0.5;

        x = Mth.lerp(x, min[0], max[0]);
        y = Mth.lerp(y, min[1], max[1]);
        z = Mth.lerp(z, min[2], max[2]);

        return new double[]{x, y + 10, z};
    }
}
