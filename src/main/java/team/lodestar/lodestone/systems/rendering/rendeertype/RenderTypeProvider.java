package team.lodestar.lodestone.systems.rendering.rendeertype;

import net.minecraft.Util;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes.*;
import team.lodestar.lodestone.systems.rendering.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;

public class RenderTypeProvider {
    private final Function<RenderTypeToken, LodestoneRenderType> function;
    private final Function<RenderTypeToken, LodestoneRenderType> simpleCache;
    private final ConcurrentHashMap<LodestoneRenderTypeKey, LodestoneRenderType> complexCache = new ConcurrentHashMap<>();

    public RenderTypeProvider(Function<RenderTypeToken, LodestoneRenderType> function) {
        this.function = function;
        this.simpleCache = Util.memoize(function);
    }

    private LodestoneRenderType createRenderType(RenderTypeToken token) {
        return simpleCache.apply(token);
    }

    private LodestoneRenderType createRenderType(LodestoneRenderTypeKey key) {
        if (complexCache.containsKey(key)) {
            return complexCache.get(key);
        }
        if (key.getModifier() != null) {
            LodestoneRenderTypes.addRenderTypeModifier(key.getModifier());
        }
        LodestoneRenderType renderType = function.apply(key.getToken());
        if (key.getUniformHandler() != null) {
            LodestoneRenderTypes.applyUniformChanges(renderType, key.getUniformHandler());
        }
        complexCache.put(key, renderType);
        return renderType;
    }

    public LodestoneRenderTypeKey apply(RenderTypeToken token) {
        return new LodestoneRenderTypeKey(this, token);
    }

    public static class LodestoneRenderTypeKey {
        private final RenderTypeProvider provider;
        private final RenderTypeToken token;
        private ShaderUniformHandler uniformHandler;
        private Consumer<LodestoneCompositeStateBuilder> modifier;

        private LodestoneRenderType renderType = null;

        public LodestoneRenderTypeKey(RenderTypeProvider provider, RenderTypeToken token) {
            this.provider = provider;
            this.token = token;
        }

        public LodestoneRenderTypeKey withUniformHandler(ShaderUniformHandler uniformHandler) {
            this.uniformHandler = uniformHandler;
            return this;
        }

        public LodestoneRenderTypeKey withModifier(Consumer<LodestoneCompositeStateBuilder> modifier) {
            this.modifier = modifier;
            return this;
        }

        public LodestoneRenderType getRenderType() {
            if (renderType != null) {
                return renderType;
            }
            renderType = provider.createRenderType(this);
            return renderType;
        }

        public RenderTypeProvider getProvider() {
            return provider;
        }

        public RenderTypeToken getToken() {
            return token;
        }

        public ShaderUniformHandler getUniformHandler() {
            return uniformHandler;
        }

        public Consumer<LodestoneCompositeStateBuilder> getModifier() {
            return modifier;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LodestoneRenderTypeKey that = (LodestoneRenderTypeKey) o;
            return Objects.equals(token, that.token) && Objects.equals(uniformHandler, that.uniformHandler) && Objects.equals(modifier, that.modifier);
        }

        @Override
        public int hashCode() {
            return Objects.hash(token, uniformHandler, modifier);
        }
    }
}