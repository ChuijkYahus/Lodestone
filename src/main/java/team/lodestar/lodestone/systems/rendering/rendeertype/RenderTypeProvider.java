package team.lodestar.lodestone.systems.rendering.rendeertype;

import team.lodestar.lodestone.registry.client.*;
import team.lodestar.lodestone.systems.rendering.*;

import java.util.concurrent.*;
import java.util.function.*;

/**
 * A provider for creating {@link LodestoneRenderType} instances.
 * Render types are cached to allow for non-static, conditional or procedural render type instances to be created
 * The cache is based on a {@link RenderTypeToken}, which will consider any added uniform handlers and modifiers as a separate token, leading to a different render type.
 */
public class RenderTypeProvider {

    private final Function<RenderTypeToken, LodestoneRenderType> provider;
    private final ConcurrentHashMap<RenderTypeToken, LodestoneRenderType> cache = new ConcurrentHashMap<>();

    public RenderTypeProvider(Function<RenderTypeToken, LodestoneRenderType> provider) {
        this.provider = provider;
    }

    /**
     * Creates a new {@link LodestoneRenderType} using the provided token and builder.
     * @param token the token to create the render type with
     * @param builder the builder containing additional properties for the render type
     * @return the created {@link LodestoneRenderType}
     */
    protected LodestoneRenderType createRenderType(RenderTypeToken token, LodestoneRenderTypeBuilder builder) {
        if (cache.containsKey(token)) {
            return cache.get(token);
        }
        if (builder.getModifier() != null) {
            LodestoneRenderTypes.addRenderTypeModifier(builder.getModifier());
        }
        var renderType = provider.apply(token);
        cache.put(token, renderType);
        if (builder.getUniformHandler() != null) {
            LodestoneRenderTypes.addUniformChanges(renderType, builder.getUniformHandler());
        }
        return renderType;
    }

    public LodestoneRenderTypeBuilder apply(RenderTypeToken token) {
        return new LodestoneRenderTypeBuilder(this, token);
    }
}