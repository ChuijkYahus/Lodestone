package team.lodestar.lodestone.systems.rendering.rendeertype;

import com.mojang.datafixers.util.*;
import net.minecraft.client.renderer.ShaderInstance;

import java.util.*;
import java.util.concurrent.*;

public class ShaderUniformHandler {

    public final ConcurrentHashMap<String, Float[]> uniformChanges = new ConcurrentHashMap<>();

    public static final ShaderUniformHandler DEPTH_FADE = new ShaderUniformHandler().withDepthFade().lock();
    public static final ShaderUniformHandler LUMITRANSPARENT = new ShaderUniformHandler().withLumiTransparency().lock();
    public static final ShaderUniformHandler LUMITRANSPARENT_DEPTH_FADE = new ShaderUniformHandler().withLumiTransparency().withDepthFade().lock();

    private boolean locked;

    public ShaderUniformHandler() {
    }

    public ShaderUniformHandler withLumiTransparency() {
        return modifyUniform("LumiTransparency", 1f);
    }

    public ShaderUniformHandler withDepthFade() {
        return modifyUniform("DepthFade", 1.5f);
    }

    public ShaderUniformHandler withDepthFade(float value) {
        return modifyUniform("DepthFade", value);
    }

    public ShaderUniformHandler modifyUniform(String uniformName, float... values) {
        if (locked || values == null || values.length == 0) {
            return this;
        }

        Float[] newValues = new Float[values.length];
        for (int i = 0; i < values.length; i++) {
            newValues[i] = values[i];
        }

        uniformChanges.merge(
                uniformName,
                newValues,
                (existing, toAppend) -> {
                    Float[] combined = Arrays.copyOf(existing, existing.length + toAppend.length);
                    System.arraycopy(toAppend, 0, combined, existing.length, toAppend.length);
                    return combined;
                }
        );

        return this;
    }

    public void updateShaderData(ShaderInstance instance) {
        for (Map.Entry<String, Float[]> uniformChange : uniformChanges.entrySet()) {
            instance.safeGetUniform(uniformChange.getKey()).set(toPrimitive(uniformChange.getValue()));
        }
    }

    private float[] toPrimitive(Float[] boxed) {
        float[] result = new float[boxed.length];
        for (int i = 0; i < boxed.length; i++) {
            result[i] = boxed[i];
        }
        return result;
    }
    public ShaderUniformHandler lock() {
        this.locked = true;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShaderUniformHandler that = (ShaderUniformHandler) o;
        return Objects.equals(uniformChanges, that.uniformChanges);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uniformChanges);
    }
}