package team.lodestar.lodestone.systems.rendering.rendeertype;

import team.lodestar.lodestone.systems.rendering.*;

import java.util.concurrent.*;
import java.util.function.*;

public class RenderTypeProvider {
    private final Function<RenderTypeToken, LodestoneRenderType> provider;
    private final ConcurrentHashMap<RenderTypeToken, LodestoneRenderTypeBuilder> cache = new ConcurrentHashMap<>();
    public RenderTypeProvider(Function<RenderTypeToken, LodestoneRenderType> provider) {
        this.provider = provider;
    }

    protected Function<RenderTypeToken, LodestoneRenderType> getProvider() {
        return provider;
    }

    public LodestoneRenderTypeBuilder apply(RenderTypeToken token) {
        return cache.computeIfAbsent(token, t -> new LodestoneRenderTypeBuilder(this, t));
    }
}