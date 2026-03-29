package team.lodestar.lodestone.modules.rendering.particle.runtime.profile;

import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleSpawnContext;

public interface ParticleSpawnProfile {
    default void apply() {
        this.apply(new ParticleSpawnContext());
    }
    void apply(ParticleSpawnContext context);
}
