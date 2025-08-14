package team.lodestar.lodestone.systems.rendering.rendeertype;

import com.mojang.datafixers.util.*;
import net.minecraft.client.renderer.ShaderInstance;

import java.util.*;
import java.util.concurrent.*;

public class ShaderUniformHandler {

    public final ConcurrentHashMap<String, Float[]> uniformChanges = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, Integer> samplerChanges = new ConcurrentHashMap<>();

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

        uniformChanges.put(uniformName, newValues);
        return this;
    }

    public ShaderUniformHandler setSamplerTexture(String samplerName, int textureId) {
        if (locked) {
            return this;
        }
        samplerChanges.put(samplerName, textureId);
        return this;
    }

    public void updateShaderData(ShaderInstance instance) {
        for (Map.Entry<String, Float[]> uniformChange : uniformChanges.entrySet()) {
            instance.safeGetUniform(uniformChange.getKey()).set(toPrimitive(uniformChange.getValue()));
        }
        for (Map.Entry<String, Integer> samplerChange : samplerChanges.entrySet()) {
            instance.setSampler(samplerChange.getKey(), samplerChange.getValue());
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
        boolean isSame;
        if (uniformChanges.size() != that.uniformChanges.size()) {
            return false;
        }
        if (samplerChanges.size() != that.samplerChanges.size()) {
            return false;
        }
        for (Map.Entry<String, Float[]> entry : uniformChanges.entrySet()) {
            Float[] otherValues = that.uniformChanges.get(entry.getKey());
            if (otherValues == null || otherValues.length != entry.getValue().length) {
                return false;
            }
            isSame = Arrays.equals(entry.getValue(), otherValues);
            if (!isSame) {
                return false;
            }
        }
        for (Map.Entry<String, Integer> entry : samplerChanges.entrySet()) {
            Integer otherValues = that.samplerChanges.get(entry.getKey());
            if (otherValues == null) {
                return false;
            }
            isSame = entry.getValue().equals(otherValues);
            if (!isSame) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;

        if (!uniformChanges.isEmpty()) {
            List<Map.Entry<String, Float[]>> uniform = new ArrayList<>(uniformChanges.entrySet());
            uniform.sort(Map.Entry.comparingByKey());
            for (Map.Entry<String, Float[]> entry : uniform) {
                int keyHash = entry.getKey().hashCode();
                int valueHash = Arrays.hashCode(entry.getValue());
                result = 31 * result + (keyHash ^ valueHash);
            }
        }
        if (!samplerChanges.isEmpty()) {
            List<Map.Entry<String, Integer>> sampler = new ArrayList<>(samplerChanges.entrySet());
            sampler.sort(Map.Entry.comparingByKey());
            for (Map.Entry<String, Integer> entry : sampler) {
                int keyHash = entry.getKey().hashCode();
                int valueHash = Objects.hashCode(entry.getValue());
                result = 31 * result + (keyHash ^ valueHash);
            }
        }



        return result;
    }
}