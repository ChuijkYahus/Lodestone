package team.lodestar.lodestone.modules.rendering.particle.component;

import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleView;

public interface PreRenderComponent {
    void preRender(int liveCount, ParticleView particles);
}