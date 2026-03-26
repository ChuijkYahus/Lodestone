package team.lodestar.lodestone.modules.rendering.particle.component;

import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleView;

public interface PreUpdateComponent {
    void preUpdate(int liveCount, float dt, ParticleView particles);
}
