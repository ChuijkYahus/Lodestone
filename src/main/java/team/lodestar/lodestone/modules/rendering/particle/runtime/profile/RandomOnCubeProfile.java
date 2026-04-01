package team.lodestar.lodestone.modules.rendering.particle.runtime.profile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleSpawnContext;

import java.util.Random;

public class RandomOnCubeProfile implements ParticleSpawnProfile {
    public static final Random RANDOM = new Random();

    private final Direction[] directions;
    private final double xPos, yPos, zPos;

    public RandomOnCubeProfile(BlockPos pos, Direction... directions) {
        this(pos.getX(), pos.getY(), pos.getZ(), directions);
    }

    public RandomOnCubeProfile(Vector3d pos, Direction... directions) {
        this(pos.x, pos.y, pos.z, directions);
    }

    public RandomOnCubeProfile(Vec3 pos, Direction... directions) {
        this(pos.x, pos.y, pos.z, directions);
    }

    public RandomOnCubeProfile(double x, double y, double z, Direction... directions) {
        this.xPos = x;
        this.yPos = y;
        this.zPos = z;
        this.directions = directions;
    }

    @Override
    public void apply(ParticleSpawnContext context, int index, int count) {
        Direction[] dirs = directions.length == 0 ? Direction.values() : directions;
        Direction direction = dirs[RANDOM.nextInt(dirs.length)];

        Direction.Axis axis = direction.getAxis();
        double d0 = 0.5625D;

        double x = axis == Direction.Axis.X ? 0.5D + d0 * direction.getStepX() : RANDOM.nextDouble();
        double y = axis == Direction.Axis.Y ? 0.5D + d0 * direction.getStepY() : RANDOM.nextDouble();
        double z = axis == Direction.Axis.Z ? 0.5D + d0 * direction.getStepZ() : RANDOM.nextDouble();

        context.position(xPos + x, yPos + y, zPos + z);
    }
}
