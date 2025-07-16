package team.lodestar.lodestone.systems.rendering.shader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.GsonHelper;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ExtendedShaderInstance extends ShaderInstance {

    protected final ShaderHolder shaderHolder;

    protected final Map<String, Consumer<Uniform>> defaultUniformData = new HashMap<>();

    public ExtendedShaderInstance(ResourceProvider pResourceProvider, ShaderHolder shaderHolder) throws IOException {
        super(pResourceProvider, shaderHolder.getShaderLocation(), shaderHolder.getShaderFormat());
        this.shaderHolder = shaderHolder;
    }

    public void setUniformDefaults() {
        for (Map.Entry<String, Consumer<Uniform>> defaultDataEntry : getDefaultUniformData().entrySet()) {
            Uniform t = uniformMap.get(defaultDataEntry.getKey());
            defaultDataEntry.getValue().accept(t);
        }
    }

    public ShaderHolder getShaderHolder() {
        return shaderHolder;
    }

    public Map<String, Consumer<Uniform>> getDefaultUniformData() {
        return defaultUniformData;
    }

    //TODO: this method sucks!!! Instead of having the shader holder define a list of uniforms to cache, we should instead be checking against a list of common uniforms that are present in most shaders and simply the default
    // Any Uniform that isn't a default minecraft uniform should be cached unless specified otherwise
    @Override
    public void parseUniformNode(JsonElement pJson) throws ChainedJsonException {
        super.parseUniformNode(pJson);

        JsonObject jsonobject = GsonHelper.convertToJsonObject(pJson, "uniform");
        String uniformName = GsonHelper.getAsString(jsonobject, "name");
        if (getShaderHolder().cachedUniforms.contains(uniformName)) {
            Uniform uniform = uniforms.getLast();

            Consumer<Uniform> consumer;
            if (uniform.getType() <= 3) {
                final IntBuffer buffer = uniform.getIntBuffer();
                buffer.position(0);
                int[] array = new int[uniform.getCount()];
                for (int i = 0; i < uniform.getCount(); i++) {
                    array[i] = buffer.get(i);
                }
                consumer = u -> {
                    buffer.position(0);
                    buffer.put(array);
                };
            } else {
                final FloatBuffer buffer = uniform.getFloatBuffer();
                buffer.position(0);
                float[] array = new float[uniform.getCount()];
                for (int i = 0; i < uniform.getCount(); i++) {
                    array[i] = buffer.get(i);
                }
                consumer = u -> {
                    buffer.position(0);
                    buffer.put(array);
                };
            }

            getDefaultUniformData().put(uniformName, consumer);
        }
    }
}
