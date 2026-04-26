package team.lodestar.lodestone.modules.rendering.particle.component.types.scale;

import team.lodestar.lodestone.modules.rendering.particle.component.PreRenderComponent;
import team.lodestar.lodestone.modules.rendering.particle.component.types.boilerplate.*;
import team.lodestar.lodestone.modules.rendering.particle.component.types.spin.*;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleView;

public class ScaleStorage extends TrinaryEasedValuesStorage<ScaleConfig> implements PreRenderComponent {

    public ScaleStorage(int capacity) {
        super(capacity);
    }

    @Override
    protected void acceptValue(int liveCount, ParticleView particles, int particleIndex, float value) {
        particles.xScale()[particleIndex] = value;
        particles.yScale()[particleIndex] = value;
        particles.zScale()[particleIndex] = value;
    }
}
