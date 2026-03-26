package team.lodestar.lodestone.modules.rendering.particle.storage;

import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleSpawnContext;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleView;

public interface ParticleComponentStorage<T> {
    void onSpawn(int particleIndex, T config, ParticleSpawnContext spawnContext, ParticleView particles);

    void onSwapRemove(int deadIndex, int movedIndex, ParticleView particles);
}