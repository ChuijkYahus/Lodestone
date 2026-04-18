package team.lodestar.lodestone.modules.rendering.particle.component.types.scale;

import team.lodestar.lodestone.modules.core.easing.Easing;
import team.lodestar.lodestone.modules.rendering.particle.component.PreRenderComponent;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleSpawnContext;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleView;
import team.lodestar.lodestone.modules.rendering.particle.storage.ParticleComponentStorage;

public class ScaleStorage implements ParticleComponentStorage<ScaleConfig>, PreRenderComponent {

    private final ScaleMode[] mode;
    private final float[] s0, s1, s2;

    private final Easing[] easing0;
    private final Easing[] easing1;

    public ScaleStorage(int capacity) {
        this.mode = new ScaleMode[capacity];
        this.s0 = new float[capacity];

        this.s1 = new float[capacity];
        this.s2 = new float[capacity];

        this.easing0 = new Easing[capacity];
        this.easing1 = new Easing[capacity];
    }

    @Override
    public void preRender(int liveCount, ParticleView particles) {
        int[] age = particles.age();
        int[] lifetime = particles.lifetime();

        for (int i = 0; i < liveCount; i++) {

            switch (mode[i]) {
                case CONSTANT -> {
                    float value = s0[i];
                    particles.xScale()[i] = value;
                    particles.yScale()[i] = value;
                    particles.zScale()[i] = value;
                }
                case LERP -> {
                    float t = lifetime[i] <= 0 ? 1.0f : (float) age[i] / (float) lifetime[i];
                    t = easing0[i].ease(t);
                    float value = lerp(s0[i], s1[i], t);
                    particles.xScale()[i] = value;
                    particles.yScale()[i] = value;
                    particles.zScale()[i] = value;
                }
                case DOUBLE_LERP -> {
                    float t = lifetime[i] <= 0 ? 1.0f : (float) age[i] / (float) lifetime[i];
                    if (t <= 0.5f) {
                        t = easing0[i].ease(t*2);
                        float value = lerp(s0[i], s1[i], t);
                        particles.xScale()[i] = value;
                        particles.yScale()[i] = value;
                        particles.zScale()[i] = value;
                    }
                    else {
                        t = easing1[i].ease((t-0.5f)*2);
                        float value = lerp(s1[i], s2[i], t);
                        particles.xScale()[i] = value;
                        particles.yScale()[i] = value;
                        particles.zScale()[i] = value;
                    }
                }
            }
        }
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    @Override
    public void onSpawn(int particleIndex, ScaleConfig config, ParticleSpawnContext spawnContext, ParticleView particles) {
        mode[particleIndex] = config.mode;

        s0[particleIndex] = config.s0;
        s1[particleIndex] = config.s1;
        s2[particleIndex] = config.s2;

        easing0[particleIndex] = config.easing0;
        easing1[particleIndex] = config.easing1;
    }

    @Override
    public void onSwapRemove(int deadIndex, int movedIndex, ParticleView particles) {
        mode[deadIndex] = mode[movedIndex];

        s0[deadIndex] = s0[movedIndex];
        s1[deadIndex] = s1[movedIndex];
        s2[deadIndex] = s2[movedIndex];

        easing0[deadIndex] = easing0[movedIndex];
        easing1[deadIndex] = easing1[movedIndex];
    }
}
