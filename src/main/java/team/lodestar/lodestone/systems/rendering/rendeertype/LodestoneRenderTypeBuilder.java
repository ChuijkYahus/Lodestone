package team.lodestar.lodestone.systems.rendering.rendeertype;

import team.lodestar.lodestone.registry.client.*;
import team.lodestar.lodestone.systems.rendering.*;

import java.util.*;
import java.util.function.*;

public class LodestoneRenderTypeBuilder {
    private final RenderTypeProvider provider;
    private RenderTypeToken token;
    private ShaderUniformHandler uniformHandler;
    private Consumer<LodestoneRenderTypes.LodestoneCompositeStateBuilder> modifier;

    public LodestoneRenderTypeBuilder(RenderTypeProvider provider, RenderTypeToken token) {
        this.provider = provider;
        this.token = token;
    }

    public LodestoneRenderTypeBuilder withUniformHandler(ShaderUniformHandler uniformHandler) {
        this.uniformHandler = uniformHandler;
        token = token.addUniformHandler(uniformHandler);
        return this;
    }

    public LodestoneRenderTypeBuilder withModifier(Consumer<LodestoneRenderTypes.LodestoneCompositeStateBuilder> modifier) {
        this.modifier = modifier;
        token = token.addModifier(modifier);
        return this;
    }

    public LodestoneRenderType getRenderType() {
        return provider.createRenderType(token, this);
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

    public Consumer<LodestoneRenderTypes.LodestoneCompositeStateBuilder> getModifier() {
        return modifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LodestoneRenderTypeBuilder that = (LodestoneRenderTypeBuilder) o;
        return Objects.equals(token, that.token) && Objects.equals(uniformHandler, that.uniformHandler) && Objects.equals(modifier, that.modifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, uniformHandler, modifier);
    }
}
