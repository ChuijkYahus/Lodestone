package team.lodestar.lodestone.modules.rendering.particle.component.types.scale;

import team.lodestar.lodestone.modules.core.easing.Easing;
import team.lodestar.lodestone.modules.rendering.particle.component.types.color.ColorMode;

import java.awt.*;
import java.util.Objects;

public class ScaleConfig {
    protected ScaleMode mode = ScaleMode.CONSTANT;

    protected float s0 = 1.0f;
    protected float s1 = 1.0f;
    protected float s2 = 1.0f;

    protected Easing easing0;
    protected Easing easing1;

    public ScaleConfig constant(float scale) {
        this.mode = ScaleMode.CONSTANT;
        this.s0 = scale;
        this.s1 = scale;
        this.s2 = scale;
        return this;
    }

    public ScaleConfig interpolate(float s0, float s1, Easing easing) {
        this.mode = ScaleMode.LERP;
        this.s0 = s0;
        this.s1 = s1;
        this.easing0 = easing;
        return this;
    }

    public ScaleConfig interpolate(float s0, float s1, float s2, Easing easing0, Easing easing1) {
        this.mode = ScaleMode.DOUBLE_LERP;
        this.s0 = s0;
        this.s1 = s1;
        this.s2 = s2;
        this.easing0 = easing0;
        this.easing1 = easing1;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ScaleConfig that = (ScaleConfig) o;
        return Float.compare(s0, that.s0) == 0 &&
                Float.compare(s1, that.s1) == 0 &&
                Float.compare(s2, that.s2) == 0 &&
                mode == that.mode &&
                Objects.equals(easing0, that.easing0) &&
                Objects.equals(easing1, that.easing1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, s0, s1, s2, easing0, easing1);
    }
}