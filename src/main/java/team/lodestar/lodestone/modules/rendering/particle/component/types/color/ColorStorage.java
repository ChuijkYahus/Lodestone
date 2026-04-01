package team.lodestar.lodestone.modules.rendering.particle.component.types.color;

import team.lodestar.lodestone.modules.core.easing.Easing;
import team.lodestar.lodestone.modules.rendering.particle.component.PreRenderComponent;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleSpawnContext;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleView;
import team.lodestar.lodestone.modules.rendering.particle.storage.ParticleComponentStorage;

public class ColorStorage implements ParticleComponentStorage<ColorConfig>, PreRenderComponent {
    private final ColorMode[] mode;

    private final float[] r0, g0, b0, a0;
    private final float[] r1, g1, b1, a1;

    private final Easing[] easing;

    private final float[] r, g, b, a;

    public ColorStorage(int capacity) {
        this.mode = new ColorMode[capacity];

        this.r0 = new float[capacity];
        this.g0 = new float[capacity];
        this.b0 = new float[capacity];
        this.a0 = new float[capacity];

        this.r1 = new float[capacity];
        this.g1 = new float[capacity];
        this.b1 = new float[capacity];
        this.a1 = new float[capacity];

        this.easing = new Easing[capacity];

        this.r = new float[capacity];
        this.g = new float[capacity];
        this.b = new float[capacity];
        this.a = new float[capacity];
    }

    @Override
    public void preRender(int liveCount, ParticleView particles) {
        int[] age = particles.age();
        int[] lifetime = particles.lifetime();

        for (int i = 0; i < liveCount; i++) {

            switch (mode[i]) {
                case ColorMode.CONSTANT -> {
                    r[i] = r0[i];
                    g[i] = g0[i];
                    b[i] = b0[i];
                    a[i] = a0[i];
                }

                case ColorMode.LERP -> {
                    float t = lifetime[i] <= 0 ? 1.0f : (float) age[i] / (float) lifetime[i];
                    t = easing[i].lerp(t, 0.0f, 1.0f);

                    r[i] = lerp(r0[i], r1[i], t);
                    g[i] = lerp(g0[i], g1[i], t);
                    b[i] = lerp(b0[i], b1[i], t);
                    a[i] = lerp(a0[i], a1[i], t);
                }

                default -> throw new IllegalStateException("Unknown color mode " + mode[i]);
            }
        }
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    @Override
    public void onSpawn(int particleIndex, ColorConfig config, ParticleSpawnContext spawnContext, ParticleView particles) {
        mode[particleIndex] = config.mode;

        switch (config.mode) {
            case ColorMode.CONSTANT -> {
                r0[particleIndex] = config.r0;
                g0[particleIndex] = config.g0;
                b0[particleIndex] = config.b0;
                a0[particleIndex] = config.a0;

                r[particleIndex] = config.r0;
                g[particleIndex] = config.g0;
                b[particleIndex] = config.b0;
                a[particleIndex] = config.a0;
            }

            case ColorMode.LERP -> {
                r0[particleIndex] = config.r0;
                g0[particleIndex] = config.g0;
                b0[particleIndex] = config.b0;
                a0[particleIndex] = config.a0;

                r1[particleIndex] = config.r1;
                g1[particleIndex] = config.g1;
                b1[particleIndex] = config.b1;
                a1[particleIndex] = config.a1;

                easing[particleIndex] = config.easing;

                r[particleIndex] = config.r0;
                g[particleIndex] = config.g0;
                b[particleIndex] = config.b0;
                a[particleIndex] = config.a0;
            }

            default -> throw new IllegalStateException("Unknown color mode: " + config.mode);
        }
    }

    @Override
    public void onSwapRemove(int deadIndex, int movedIndex, ParticleView particles) {
        mode[deadIndex] = mode[movedIndex];

        r0[deadIndex] = r0[movedIndex];
        g0[deadIndex] = g0[movedIndex];
        b0[deadIndex] = b0[movedIndex];
        a0[deadIndex] = a0[movedIndex];

        r1[deadIndex] = r1[movedIndex];
        g1[deadIndex] = g1[movedIndex];
        b1[deadIndex] = b1[movedIndex];
        a1[deadIndex] = a1[movedIndex];

        easing[deadIndex] = easing[movedIndex];

        r[deadIndex] = r[movedIndex];
        g[deadIndex] = g[movedIndex];
        b[deadIndex] = b[movedIndex];
        a[deadIndex] = a[movedIndex];
    }

    public float[] r() {
        return r;
    }

    public float[] g() {
        return g;
    }

    public float[] b() {
        return b;
    }

    public float[] a() {
        return a;
    }
}
