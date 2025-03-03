package team.lodestar.lodestone.systems.particle.world.behaviors;

import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.particle.data.*;

public class ExtrudingSparkParticleBehavior extends SparkParticleBehavior {

    public ExtrudingSparkParticleBehavior(GenericParticleData lengthData) {
        super(lengthData);
    }

    public ExtrudingSparkParticleBehavior() {
        this(null);
    }

    @Override
    public Vec3 sparkStart(Vec3 pos, Vec3 offset) {
        return pos;
    }

    @Override
    public Vec3 sparkEnd(Vec3 pos, Vec3 offset) {
        return pos.add(offset.x * 2, offset.y * 2, offset.z * 2);
    }
}