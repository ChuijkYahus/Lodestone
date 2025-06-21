package team.lodestar.lodestone.systems.rendering.rendeertype;

import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import team.lodestar.lodestone.registry.client.*;

import java.util.*;
import java.util.function.*;

public class RenderTypeToken implements Supplier<RenderStateShard.EmptyTextureStateShard> {

    private static final HashMap<ResourceLocation, RenderTypeToken> CACHED_TEXTURE_TOKENS = new HashMap<>();
    private static final HashMap<RenderStateShard.EmptyTextureStateShard, RenderTypeToken> CACHED_STATE_TOKENS = new HashMap<>();

    private final UUID identifier;
    private final RenderStateShard.EmptyTextureStateShard texture;

    protected RenderTypeToken(ResourceLocation texture) {
        this(new RenderStateShard.TextureStateShard(texture, false, false));
    }

    protected RenderTypeToken(RenderStateShard.EmptyTextureStateShard texture) {
        this.identifier = UUID.randomUUID();
        this.texture = texture;
    }

    public RenderTypeToken(UUID identifier, RenderStateShard.EmptyTextureStateShard texture) {
        this.identifier = identifier;
        this.texture = texture;
    }

    public static RenderTypeToken createToken(ResourceLocation texture) {
        return CACHED_TEXTURE_TOKENS.computeIfAbsent(texture, RenderTypeToken::new);
    }

    public static RenderTypeToken createToken(RenderStateShard.EmptyTextureStateShard texture) {
        return CACHED_STATE_TOKENS.computeIfAbsent(texture, RenderTypeToken::new);
    }

    protected ComplexRenderTypeToken addUniformHandler(ShaderUniformHandler uniformHandler) {
        return new ComplexRenderTypeToken(this).addUniformHandler(uniformHandler);
    }

    protected ComplexRenderTypeToken addModifier(Consumer<LodestoneRenderTypes.LodestoneCompositeStateBuilder> modifier) {
        return new ComplexRenderTypeToken(this).addModifier(modifier);
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public RenderStateShard.EmptyTextureStateShard getTexture() {
        return texture;
    }

    @Override
    public RenderStateShard.EmptyTextureStateShard get() {
        return texture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenderTypeToken that = (RenderTypeToken) o;
        return Objects.equals(identifier, that.identifier) && Objects.equals(texture, that.texture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, texture);
    }
}