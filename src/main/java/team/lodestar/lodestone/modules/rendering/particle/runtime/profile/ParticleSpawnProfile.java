package team.lodestar.lodestone.modules.rendering.particle.runtime.profile;

import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleSpawnContext;

public interface ParticleSpawnProfile {
    void apply(ParticleSpawnContext context, int index, int count);

    default void apply(ParticleSpawnContext context) {
        apply(context, 0, 1);
    }
}
