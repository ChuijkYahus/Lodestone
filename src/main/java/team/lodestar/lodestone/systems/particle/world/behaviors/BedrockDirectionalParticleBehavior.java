package team.lodestar.lodestone.systems.particle.world.behaviors;

import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.particle.builder.*;
import team.lodestar.lodestone.systems.particle.data.spin.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.options.*;

public class BedrockDirectionalParticleBehavior extends DirectionalParticleBehavior {

    private final SpinParticleData pitchData;
    private final SpinParticleData yawData;

    public float pitch;
    public float yaw;

    public BedrockDirectionalParticleBehavior(SpinParticleData pitchData, SpinParticleData yawData) {
        this.pitchData = pitchData;
        this.yawData = yawData;
        this.pitch = pitchData.spinOffset + pitchData.startingValue;
        this.yaw = yawData.spinOffset + yawData.startingValue;
    }

    public BedrockDirectionalParticleBehavior() {
        this.pitchData = null;
        this.yawData = null;
    }

    public BedrockDirectionalParticleBehavior(SpinParticleData data) {
        this(data, data);
    }

    @Override
    public Vec3 getDirection(LodestoneWorldParticle particle) {
        float x = (float) (Math.cos(pitch) * Math.cos(yaw));
        float y = (float) Math.sin(pitch);
        float z = (float) (Math.cos(pitch) * Math.sin(yaw));
        return new Vec3(x, y, z).normalize();
    }

    @Override
    public void tick(LodestoneWorldParticle particle) {
        pitch += getPitchData(particle.spinData).getValue(particle.getAge(), particle.getLifetime());
        yaw += getYawData(particle.spinData).getValue(particle.getAge(), particle.getLifetime());
    }

    public SpinParticleData getPitchData(AbstractParticleBuilder<WorldParticleOptions> builder) {
        return getPitchData(builder.getSpinData());
    }

    public SpinParticleData getYawData(AbstractParticleBuilder<WorldParticleOptions> builder) {
        return getYawData(builder.getSpinData());
    }

    public SpinParticleData getPitchData(SpinParticleData delegate) {
        return pitchData != null ? pitchData : delegate;
    }

    public SpinParticleData getYawData(SpinParticleData delegate) {
        return yawData != null ? yawData : delegate;
    }
}