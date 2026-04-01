package team.lodestar.lodestone.modules.core.easing;

import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import java.util.HashMap;

@SuppressWarnings("unused")
public abstract class Easing {

    public static final Codec<Easing> CODEC = Codec.STRING.xmap(Easing::valueOf, e -> e.name);

    public static final HashMap<String, Easing> EASINGS = new HashMap<>();
    public final String name;

    public Easing(String name) {
        this.name = name;
        EASINGS.put(name, this);
    }

    public static Easing valueOf(String name) {
        return EASINGS.get(name);
    }

    public abstract float ease(float delta);

    protected float pow(float delta, float exponent) {
        return (float) Math.pow(delta, exponent);
    }

    protected float exponentOut(float delta, float exponent) {
        return 1 - pow(1 - delta, exponent);
    }

    protected float exponentInOut(float delta, float exponent) {
        return delta < 0.5
                ? (exponent * 2) * pow(delta, exponent)
                : 1 - pow(-2 * delta + 2, exponent) / 2;
    }

    public float lerp(float delta, float min, float max) {
        var pct = ease(delta);
        return Mth.lerp(pct, min, max);
    }

    public double lerp(float delta, double min, double max) {
        var pct = ease(delta);
        return Mth.lerp(pct, min, max);
    }

    public float asValueDistribution(float delta, float start, float middle, float end) {
        float offset = Mth.abs(0.5f - delta) / 0.5f;
        float easedOffset = ease(1 - offset);
        if (delta < 0.5f) {
            return Mth.lerp(easedOffset, start, middle);
        } else {
            return Mth.lerp(1 - easedOffset, middle, end);
        }
    }

    public float asValueDistribution(float delta, float start, float end) {
        return asValueDistribution(delta, start, (start+end)/2f, end);
    }

    public float asWeighedRandom(RandomSource randomSource, float start, float middle, float end) {
        return asValueDistribution(randomSource.nextFloat(), start, middle, end);
    }

    public float asWeighedRandom(RandomSource randomSource, float start, float end) {
        return asValueDistribution(randomSource.nextFloat(), start, end);
    }

    public static final Easing LINEAR = new Easing("linear") {

        public float ease(float delta) {
            return delta;
        }
    };

    public static final Easing SINE_IN = new Easing("sineIn") {

        public float ease(float delta) {
            return (1 - Mth.cos((delta * Mth.PI) / 2));
        }
    };

    public static final Easing SINE_OUT = new Easing("sineOut") {

        public float ease(float delta) {
            return Mth.sin((delta * Mth.PI) / 2);
        }
    };

    public static final Easing SINE_IN_OUT = new Easing("sineInOut") {

        public float ease(float delta) {
            return (-(Mth.cos(Mth.PI * delta) - 1) / 2);
        }
    };

    public static final Easing QUAD_IN = new Easing("quadIn") {

        public float ease(float delta) {
            return pow(delta, 2);
        }
    };

    public static final Easing QUAD_OUT = new Easing("quadOut") {

        public float ease(float delta) {
            return exponentOut(delta, 2);
        }
    };

    public static final Easing QUAD_IN_OUT = new Easing("quadInOut") {

        public float ease(float delta) {
            return exponentInOut(delta, 2);
        }
    };

    public static final Easing CUBIC_IN = new Easing("cubicIn") {

        public float ease(float delta) {
            return pow(delta, 3);
        }
    };

    public static final Easing CUBIC_OUT = new Easing("cubicOut") {

        public float ease(float delta) {
            return exponentOut(delta, 3);
        }
    };

    public static final Easing CUBIC_IN_OUT = new Easing("cubicInOut") {

        public float ease(float delta) {
            return exponentInOut(delta, 3);
        }
    };

    public static final Easing QUARTIC_IN = new Easing("quarticIn") {

        public float ease(float delta) {
            return pow(delta, 4);
        }
    };

    public static final Easing QUARTIC_OUT = new Easing("quarticOut") {

        public float ease(float delta) {
            return exponentOut(delta, 4);
        }
    };

    public static final Easing QUARTIC_IN_OUT = new Easing("quarticInOut") {

        public float ease(float delta) {
            return exponentInOut(delta, 4);
        }
    };

    public static final Easing QUINTIC_IN = new Easing("quinticIn") {

        public float ease(float delta) {
            return pow(delta, 5);
        }
    };

    public static final Easing QUINTIC_OUT = new Easing("quinticOut") {

        public float ease(float delta) {
            return exponentOut(delta, 5);
        }
    };

    public static final Easing QUINTIC_IN_OUT = new Easing("quinticInOut") {

        public float ease(float delta) {
            return exponentInOut(delta, 5);
        }
    };

    public static final Easing EXPO_IN = new Easing("expoIn") {
        public float ease(float delta) {
            return delta == 0
                    ? 0
                    : (float) pow(2, 10 * delta - 10);
        }
    };

    public static final Easing EXPO_OUT = new Easing("expoOut") {
        public float ease(float delta) {
            return delta == 1
                    ? 1
                    : (float) (1 - pow(2, -10 * delta));
        }
    };

    public static final Easing EXPO_IN_OUT = new Easing("expoInOut") {
        public float ease(float delta) {
            if (delta <= 0) {
                return 0;
            }
            if (delta >= 1) {
                return 1;
            }
            return delta < 0.5f
                    ? (float) pow(2, 20 * delta - 10) / 2f
                    : (float) (2 - pow(2, -20 * delta + 10)) / 2f;
        }
    };

    public static final Easing CIRC_IN = new Easing("circIn") {
        public float ease(float delta) {
            return (float) (1 - Math.sqrt(1 - pow(delta, 2)));
        }
    };

    public static final Easing CIRC_OUT = new Easing("circOut") {
        public float ease(float delta) {
            return (float) Math.sqrt(1 - pow(delta - 1, 2));
        }
    };

    public static final Easing CIRC_IN_OUT = new Easing("circInOut") {
        public float ease(float delta) {
            return delta < 0.5f
                    ? (float) (1 - Math.sqrt(1 - pow(2 * delta, 2))) / 2f
                    : (float) (Math.sqrt(1 - pow(-2 * delta + 2, 2)) + 1) / 2f;
        }
    };

    public static abstract class Back extends Easing {

        public static final float DEFAULT_OVERSHOOT = 1.70158f;

        private float overshoot;

        public Back(String name) {
            this(name, DEFAULT_OVERSHOOT);
        }

        public Back(String name, float overshoot) {
            super(name);
            this.overshoot = overshoot;
        }

        public void setOvershoot(float overshoot) {
            this.overshoot = overshoot;
        }

        public float getOvershoot() {
            return overshoot;
        }
    }

    public static final Easing BACK_IN = new Easing("backIn") {

        @Override
        public float ease(float delta) {
            float s = 1.70158f;
            float c = s + 1;
            return c * pow(delta, 3) - s * pow(delta, 2);
        }
    };

    public static final Easing BACK_OUT = new Easing("backOut") {
        @Override
        public float ease(float delta) {
            float s = 1.70158f;
            float c = s + 1;
            return 1 + c * pow(delta - 1, 3) + s * pow(delta - 1, 2);
        }
    };

    public static final Easing BACK_IN_OUT = new Easing("backInOut") {

        @Override
        public float ease(float delta) {
            float s = 1.70158f;
            float c = s * 1.525f;
            return delta < 0.5f
                    ? (pow(2 * delta, 2) * ((c + 1) * 2 * delta - c)) / 2
                    : (pow(2 * delta - 2, 2) * ((c + 1) * (delta * 2 - 2) + c) + 2) / 2;
        }
    };

    public static final Easing ELASTIC_IN = new Easing("elasticIn"){

        @Override
        public float ease(float delta) {
            if (delta <= 0) {
                return 0;
            }
            if (delta >= 1) {
                return 1;
            }
            float c = (2 * Mth.PI) / 3f;
            return -pow(2, 10 * delta - 10) * Mth.sin((delta * 10 - 10.75f) * c);
        }
    };

    public static final Easing ELASTIC_OUT = new Easing("elasticOut") {

        @Override
        public float ease(float delta) {
            if (delta <= 0) {
                return 0;
            }
            if (delta >= 1) {
                return 1;
            }
            float c = (2 * Mth.PI) / 3f;
            return -pow(2, -10 * delta) * Mth.sin((delta * 10 - 0.75f) * c) + 1;
        }
    };

    public static final Easing ELASTIC_IN_OUT = new Easing("elasticInOut") {

        @Override
        public float ease(float delta) {
            if (delta <= 0) {
                return 0;
            }
            if (delta >= 1) {
                return 1;
            }

            float c = (2 * Mth.PI) / 4.5f;
            return delta < 0.5
                    ? -(pow(2, 20 * delta - 10) * Mth.sin((20 * delta - 11.125f) * c)) / 2
                    : (pow(2, -20 * delta + 10) * Mth.sin((20 * delta - 11.125f) * c)) / 2 + 1;
        }
    };

    public static final Easing BOUNCE_IN = new Easing("bounceIn") {
        public float ease(float delta) {
            return 1 - BOUNCE_OUT.ease(1 - delta);
        }
    };

    public static final Easing BOUNCE_OUT = new Easing("bounceOut") {
        public float ease(float delta) {
            float n1 = 7.5625f;
            float d1 = 2.75f;

            if (delta < 1 / d1) {
                return n1 * delta * delta;
            } else if (delta < 2 / d1) {
                return n1 * (delta -= 1.5f / d1) * delta + 0.75f;
            } else if (delta < 2.5 / d1) {
                return n1 * (delta -= 2.25f / d1) * delta + 0.9375f;
            } else {
                return n1 * (delta -= 2.625f / d1) * delta + 0.984375f;
            }
        }
    };

    public static final Easing BOUNCE_IN_OUT = new Easing("bounceInOut") {
        public float ease(float delta) {
            return delta < 0.5
                    ? (1 - BOUNCE_OUT.ease(1 - 2 * delta)) / 2
                    : (1 + BOUNCE_OUT.ease(2 * delta - 1)) / 2;
        }
    };
}