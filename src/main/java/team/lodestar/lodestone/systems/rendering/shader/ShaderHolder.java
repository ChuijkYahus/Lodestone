package team.lodestar.lodestone.systems.rendering.shader;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import team.lodestar.lodestone.LodestoneLib;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ShaderHolder implements LodestoneShader {

    protected final ResourceLocation shaderLocation;
    protected final VertexFormat shaderFormat;

    protected ExtendedShaderInstance shaderInstance;
    protected Collection<String> cachedUniforms;

    private final RenderStateShard.ShaderStateShard shard = new RenderStateShard.ShaderStateShard(getInstance());

    public ShaderHolder(ResourceLocation shaderLocation, VertexFormat shaderFormat, String... cachedUniforms) {
        this.shaderLocation = shaderLocation;
        this.shaderFormat = shaderFormat;
        this.cachedUniforms = List.of(cachedUniforms);
    }

    public ExtendedShaderInstance createInstance(ResourceProvider provider) throws IOException {
        return shaderInstance = new ExtendedShaderInstance(provider, this);
    }

    public ResourceLocation getShaderLocation() {
        return shaderLocation;
    }

    public VertexFormat getShaderFormat() {
        return shaderFormat;
    }

    public ExtendedShaderInstance getShaderInstance() {
        return shaderInstance;
    }

    public Collection<String> getCachedUniforms() {
        return cachedUniforms;
    }

    public Supplier<ShaderInstance> getInstance() {
        return () -> shaderInstance;
    }

    public void setShaderInstance(ShaderInstance reloadedShaderInstance) {
        this.shaderInstance = (ExtendedShaderInstance) reloadedShaderInstance;
    }

    public RenderStateShard.ShaderStateShard getShard() {
        return shard;
    }

    @Override
    public void register(RegisterShadersEvent event) {
        try {
            ResourceProvider provider = event.getResourceProvider();
            event.registerShader(this.createInstance(provider), this::setShaderInstance);
        } catch (IOException e) {
            LodestoneLib.LOGGER.error("Error registering shader", e);
            e.printStackTrace();
        }
    }
}