package team.lodestar.lodestone.modules.rendering.particle.runtime.profile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Random;
import org.joml.Vector3d;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleSpawnContext;

public class RandomInCubeProfile implements ParticleSpawnProfile {
    private double xMin, yMin, zMin;
    private double xMax, yMax, zMax;
    private Random random = new Random();

    public RandomInCubeProfile(Vec3 center, double size) {
        this(center.x - size / 2, center.y - size / 2, center.z - size / 2,
             center.x + size / 2, center.y + size / 2, center.z + size / 2);
    }

    public RandomInCubeProfile(AABB aabb) {
        this(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    public RandomInCubeProfile(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }

    public RandomInCubeProfile(Vector3d min, Vector3d max) {
        this(min.x, min.y, min.z, max.x, max.y, max.z);
    }

    public RandomInCubeProfile(Vec3 min, Vec3 max) {
        this(min.x, min.y, min.z, max.x, max.y, max.z);
    }

    public RandomInCubeProfile(double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.zMin = zMin;
        this.xMax = xMax;
        this.yMax = yMax;
        this.zMax = zMax;
    }

    @Override
    public void apply(ParticleSpawnContext context, int index, int count) {
        double x = xMin + random.nextFloat() * (xMax - xMin);
        double y = yMin + random.nextFloat() * (yMax - yMin);
        double z = zMin + random.nextFloat() * (zMax - zMin);
        context.position(x, y, z);
    }
}
