package team.lodestar.lodestone.systems.rendering.rendeertype;

import com.mojang.datafixers.util.*;
import net.minecraft.client.renderer.ShaderInstance;

import java.util.*;
import java.util.concurrent.*;

public class ShaderUniformHandler {

    public final ConcurrentHashMap<String, Float> uniformChanges = new ConcurrentHashMap<>();

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

    public ShaderUniformHandler modifyUniform(String uniformName, float value) {
        if (locked) {
            return this;
        }
        uniformChanges.put(uniformName, value);
        return this;
    }

    public void updateShaderData(ShaderInstance instance) {
        for (Map.Entry<String, Float> uniformChange : uniformChanges.entrySet()) {
            instance.safeGetUniform(uniformChange.getKey()).set(uniformChange.getValue());
        }
    }

    public ShaderUniformHandler lock() {
        this.locked = true;
        return this;
    }
}