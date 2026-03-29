package team.lodestar.lodestone.modules.rendering.particle.runtime;

import team.lodestar.lodestone.modules.rendering.particle.runtime.profile.ParticleSpawnProfile;

public class ParticleSpawnContextChain {
    private ParticleSpawnProfile[] profiles;

    public ParticleSpawnContextChain(ParticleSpawnProfile... profiles) {
        this.profiles = profiles;
    }

    public ParticleSpawnContext apply(ParticleSpawnContext ctx) {
        for (ParticleSpawnProfile profile : profiles) {
            profile.apply(ctx);
        }
        return ctx;
    }
}
