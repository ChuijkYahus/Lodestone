package team.lodestar.lodestone.systems.particle.world.behaviors;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.rendering.*;

public class SparkParticleBehavior implements LodestoneParticleBehavior {

    private static final VFXBuilders.WorldVFXBuilder SPARK_BUILDER = VFXBuilders.createWorld().setFormat(DefaultVertexFormat.PARTICLE);

    protected final GenericParticleData lengthData;
    protected Vec3 forcedDirection;
    protected Vec3 cachedDirection;

    public SparkParticleBehavior(GenericParticleData lengthData) {
        this.lengthData = lengthData;
    }

    public SparkParticleBehavior() {
        this(null);
    }

    @Override
    public void render(LodestoneWorldParticle particle, VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 vec3 = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, particle.getXOld(), particle.getX()) - vec3.x());
        float y = (float) (Mth.lerp(partialTicks, particle.getYOld(), particle.getY()) - vec3.y());
        float z = (float) (Mth.lerp(partialTicks, particle.getZOld(), particle.getZ()) - vec3.z());
        final Vec3 pos = new Vec3(x, y, z);
        var lengthData = getLengthData(particle);
        float length = lengthData.getValue(particle.getAge(), particle.getLifetime());
        Vec3 offset = getDirection(particle).scale(length);
        Vec3 movingFrom = sparkStart(pos, offset);
        Vec3 movingTo = sparkEnd(pos, offset);
        SPARK_BUILDER.setVertexConsumer(consumer)
                .setUV(particle.getU0(), particle.getV0(), particle.getU1(), particle.getV1())
                .setColor(particle.getRed(), particle.getGreen(), particle.getBlue())
                .setAlpha(particle.getAlpha())
                .renderBeam(null, movingFrom, movingTo, particle.getQuadSize(partialTicks), Vec3.ZERO);
    }

    @Override
    public void tick(LodestoneWorldParticle particle) {
        var direction = particle.getParticleSpeed().normalize();
        if (!direction.equals(Vec3.ZERO)) {
            cachedDirection = direction;
        }
    }

    public SparkParticleBehavior setForcedDirection(Vec3 forcedDirection) {
        this.forcedDirection = forcedDirection;
        return this;
    }

    public GenericParticleData getLengthData(LodestoneWorldParticle particle) {
        return getLengthData() != null ? getLengthData() : particle.scaleData;
    }

    public Vec3 getDirection(LodestoneWorldParticle particle) {
        if (forcedDirection != null) {
            return forcedDirection;
        }
        return getCachedDirection() != null ? getCachedDirection() : particle.getParticleSpeed().normalize();
    }

    public GenericParticleData getLengthData() {
        return lengthData;
    }

    public Vec3 getCachedDirection() {
        return cachedDirection;
    }

    public Vec3 sparkStart(Vec3 pos, Vec3 offset) {
        return pos.subtract(offset);
    }

    public Vec3 sparkEnd(Vec3 pos, Vec3 offset) {
        return pos.add(offset);
    }
}