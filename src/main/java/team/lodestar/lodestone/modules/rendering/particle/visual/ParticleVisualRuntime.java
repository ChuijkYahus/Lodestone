package team.lodestar.lodestone.modules.rendering.particle.visual;

import team.lodestar.lodestone.modules.rendering.particle.pool.ParticlePool;

public interface ParticleVisualRuntime {
    void collect(ParticleVisualCollectContext context, ParticlePool pool, int liveCount);
}