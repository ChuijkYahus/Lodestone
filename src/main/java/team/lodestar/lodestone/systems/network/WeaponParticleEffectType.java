package team.lodestar.lodestone.systems.network;

import net.minecraft.nbt.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.neoforged.api.distmarker.*;
import team.lodestar.lodestone.helpers.*;
import team.lodestar.lodestone.systems.network.particle.NetworkedParticleEffectColorData;
import team.lodestar.lodestone.systems.network.particle.NetworkedParticleEffectExtraData;
import team.lodestar.lodestone.systems.network.particle.NetworkedParticleEffectPositionData;
import team.lodestar.lodestone.systems.network.particle.NetworkedParticleEffectType;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.data.spin.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;

import java.util.function.*;

public abstract class WeaponParticleEffectType extends NetworkedParticleEffectType {

    public WeaponParticleEffectType(String id) {
        super(id);
    }

    public static NetworkedParticleEffectExtraData createData(Vec3 direction, boolean mirror, float angle) {
        CompoundTag tag = new CompoundTag();
        CompoundTag directionTag = new CompoundTag();
        directionTag.putDouble("x", direction.x);
        directionTag.putDouble("y", direction.y);
        directionTag.putDouble("z", direction.z);
        tag.putFloat("angle", angle);
        tag.putBoolean("mirror", mirror);
        tag.put("direction", directionTag);
        return new NetworkedParticleEffectExtraData(tag);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public final Supplier<ParticleEffectActor> get() {
        return () -> (level, random, positionData, colorData, nbtData) -> {
            if (!nbtData.compoundTag.contains("direction")) {
                return;
            }
            final CompoundTag directionData = nbtData.compoundTag.getCompound("direction");
            double dirX = directionData.getDouble("x");
            double dirY = directionData.getDouble("y");
            double dirZ = directionData.getDouble("z");
            Vec3 direction = new Vec3(dirX, dirY, dirZ);
            float angle = nbtData.compoundTag.getFloat("angle");
            boolean mirror = nbtData.compoundTag.getBoolean("mirror");
            float spinOffset = angle + RandomHelper.randomBetween(random, -0.5f, 0.5f) + (mirror ? 3.14f : 0);

            spawnParticles(level, random, positionData, colorData, nbtData, direction, angle, mirror, spinOffset);
        };
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void spawnParticles(Level level, RandomSource random, NetworkedParticleEffectPositionData positionData,
                                        NetworkedParticleEffectColorData colorData, NetworkedParticleEffectExtraData nbtData,
                                        Vec3 direction, float angle, boolean mirror, float spinOffset);
}