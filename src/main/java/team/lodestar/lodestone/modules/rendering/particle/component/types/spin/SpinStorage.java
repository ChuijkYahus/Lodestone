package team.lodestar.lodestone.modules.rendering.particle.component.types.spin;

import team.lodestar.lodestone.modules.rendering.particle.component.*;
import team.lodestar.lodestone.modules.rendering.particle.component.types.boilerplate.*;
import team.lodestar.lodestone.modules.rendering.particle.runtime.*;

public class SpinStorage extends TrinaryEasedValuesStorage<SpinConfig> implements PreRenderComponent {

    public SpinStorage(int capacity) {
        super(capacity);
    }

    @Override
    protected void acceptValue(int liveCount, ParticleView particles, int particleIndex, float value) {
        particles.spin()[particleIndex] += value;
    }
}
