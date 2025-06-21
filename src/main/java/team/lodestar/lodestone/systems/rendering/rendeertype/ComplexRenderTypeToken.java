package team.lodestar.lodestone.systems.rendering.rendeertype;

import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import team.lodestar.lodestone.registry.client.*;

import java.util.*;
import java.util.function.*;

public class ComplexRenderTypeToken extends RenderTypeToken {

    private ShaderUniformHandler uniformHandler;
    private Consumer<LodestoneRenderTypes.LodestoneCompositeStateBuilder> modifier;

    protected ComplexRenderTypeToken(ResourceLocation texture) {
        super(texture);
    }

    protected ComplexRenderTypeToken(RenderStateShard.EmptyTextureStateShard texture) {
        super(texture);
    }

    @Override
    protected ComplexRenderTypeToken addUniformHandler(ShaderUniformHandler uniformHandler) {
        this.uniformHandler = uniformHandler;
        return this;
    }

    @Override
    protected ComplexRenderTypeToken addModifier(Consumer<LodestoneRenderTypes.LodestoneCompositeStateBuilder> modifier) {
        this.modifier = modifier;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ComplexRenderTypeToken that = (ComplexRenderTypeToken) o;
        return Objects.equals(uniformHandler, that.uniformHandler) && Objects.equals(modifier, that.modifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uniformHandler, modifier);
    }
}