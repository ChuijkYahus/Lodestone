package team.lodestar.lodestone.systems.rendering.shader;

import com.google.gson.JsonArray;
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
import java.io.Reader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.function.Consumer;

public class ExtendedShaderInstance extends ShaderInstance {

    private static final Set<String> EXCLUDED_UNIFORMS = Set.of(
            "ModelViewMat",
            "ProjMat",
            "TextureMat",
            "ScreenSize",
            "ColorModulator",
            "Light0_Direction",
            "Light1_Direction",
            "GlintAlpha",
            "FogStart",
            "FogEnd",
            "FogColor",
            "FogShape",
            "LineWidth",
            "GameTime",
            "ChunkOffset"
    );

    protected final ShaderHolder shaderHolder;

    protected final Map<String, Consumer<Uniform>> defaultUniformData = new HashMap<>();

    public ExtendedShaderInstance(ResourceProvider pResourceProvider, ShaderHolder shaderHolder) throws IOException {
        super(pResourceProvider, shaderHolder.getShaderLocation(), shaderHolder.getShaderFormat());
        this.shaderHolder = shaderHolder;
        var jsonLocation = shaderHolder.getShaderLocation().withPath(p -> "shaders/core/" + p + ".json");
        try (Reader reader = pResourceProvider.openAsReader(jsonLocation)) {
            JsonObject shaderJson = GsonHelper.parse(reader);
            parseDefaultUniformValues(shaderJson);
        }
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

    public void parseDefaultUniformValues(JsonObject shaderJson) {
        var shaderUniforms = GsonHelper.getAsJsonArray(shaderJson, "uniforms", null);
        if (shaderUniforms == null) {
            return;
        }
        for (JsonElement uniformJson : shaderUniforms) {
            JsonObject uniformObject = GsonHelper.convertToJsonObject(uniformJson, "uniform");
            var uniformName = GsonHelper.getAsString(uniformObject, "name");
            if (EXCLUDED_UNIFORMS.contains(uniformName)) {
                return;
            }
            Uniform uniform = uniformMap.get(uniformName);

            Consumer<Uniform> consumer;
            if (uniform.getType() <= 3) {
                IntBuffer buffer = uniform.getIntBuffer();
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
                FloatBuffer buffer = uniform.getFloatBuffer();
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
