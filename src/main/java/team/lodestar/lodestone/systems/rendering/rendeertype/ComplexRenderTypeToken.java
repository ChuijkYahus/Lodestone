package team.lodestar.lodestone.systems.rendering.rendeertype;

import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import team.lodestar.lodestone.registry.client.*;

import java.util.*;
import java.util.function.*;

public class ComplexRenderTypeToken extends RenderTypeToken {

    private ShaderUniformHandler uniformHandler;
    private Consumer<LodestoneRenderTypes.LodestoneCompositeStateBuilder> modifier;

    public ComplexRenderTypeToken(RenderTypeToken token) {
        super(token.getIdentifier(), token.getTexture());
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
        if (uniformHandler == null && modifier == null) {
            return super.equals(o);
        }
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ComplexRenderTypeToken that = (ComplexRenderTypeToken) o;
        final boolean equals = Objects.equals(uniformHandler, that.uniformHandler);
        final boolean equals1 = Objects.equals(modifier, that.modifier);
        return equals && equals1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uniformHandler, modifier);
    }
}