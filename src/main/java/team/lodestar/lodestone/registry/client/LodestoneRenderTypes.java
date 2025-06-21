package team.lodestar.lodestone.registry.client;

import com.mojang.blaze3d.platform.*;
import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.systems.rendering.*;
import team.lodestar.lodestone.systems.rendering.rendeertype.*;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import java.util.*;
import java.util.function.*;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.*;
import static com.mojang.blaze3d.vertex.VertexFormat.Mode.*;

@SuppressWarnings({"deprecation", "unused", "UnusedReturnValue"})
public class LodestoneRenderTypes extends RenderStateShard {

    public static final Runnable TRANSPARENT_FUNCTION = () -> RenderSystem.blendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

    public static final Runnable ADDITIVE_FUNCTION = () -> RenderSystem.blendFunc(
            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

    public static final EmptyTextureStateShard NO_TEXTURE = RenderStateShard.NO_TEXTURE;
    public static final LightmapStateShard LIGHTMAP = RenderStateShard.LIGHTMAP;
    public static final LightmapStateShard NO_LIGHTMAP = RenderStateShard.NO_LIGHTMAP;
    public static final CullStateShard CULL = RenderStateShard.CULL;
    public static final CullStateShard NO_CULL = RenderStateShard.NO_CULL;
    public static final WriteMaskStateShard COLOR_DEPTH_WRITE = RenderStateShard.COLOR_DEPTH_WRITE;
    public static final WriteMaskStateShard COLOR_WRITE = RenderStateShard.COLOR_WRITE;

    public LodestoneRenderTypes(String p_110161_, Runnable p_110162_, Runnable p_110163_) {
        super(p_110161_, p_110162_, p_110163_);
    }

    public static final HashMap<Pair<Object, LodestoneRenderType>, LodestoneRenderType> COPIES = new HashMap<>();
    public static final Function<RenderTypeData, LodestoneRenderType> GENERIC = RenderTypeData::createRenderType;

    private static Consumer<LodestoneCompositeStateBuilder> MODIFIER;

    public static final LodestoneRenderType ADDITIVE_PARTICLE = createGenericRenderType("additive_particle", PARTICLE, QUADS,
            builder(TextureAtlas.LOCATION_PARTICLES, StateShards.ADDITIVE_TRANSPARENCY, LodestoneShaders.PARTICLE, NO_CULL, LIGHTMAP, COLOR_WRITE));

    public static final LodestoneRenderType ADDITIVE_BLOCK_PARTICLE = createGenericRenderType("additive_block_particle", PARTICLE, QUADS,
            builder(TextureAtlas.LOCATION_BLOCKS, StateShards.ADDITIVE_TRANSPARENCY, LodestoneShaders.PARTICLE, NO_CULL, LIGHTMAP, COLOR_WRITE));

    public static final LodestoneRenderType ADDITIVE_BLOCK = createGenericRenderType("additive_block", POSITION_COLOR_TEX_LIGHTMAP, QUADS,
            builder(TextureAtlas.LOCATION_BLOCKS, StateShards.ADDITIVE_TRANSPARENCY, LodestoneShaders.LODESTONE_TEXTURE, NO_CULL, LIGHTMAP, COLOR_WRITE));

    public static final LodestoneRenderType ADDITIVE_SOLID = createGenericRenderType("additive_solid", POSITION_COLOR_LIGHTMAP, QUADS,
            builder(StateShards.ADDITIVE_TRANSPARENCY, POSITION_COLOR_LIGHTMAP_SHADER, NO_CULL, LIGHTMAP, COLOR_WRITE));

    public static final LodestoneRenderType TRANSPARENT_PARTICLE = createGenericRenderType("transparent_particle", PARTICLE, QUADS,
            builder(TextureAtlas.LOCATION_PARTICLES, StateShards.NORMAL_TRANSPARENCY, LodestoneShaders.PARTICLE, NO_CULL, LIGHTMAP, COLOR_WRITE));

    public static final LodestoneRenderType TRANSPARENT_BLOCK_PARTICLE = createGenericRenderType("transparent_block_particle", PARTICLE, QUADS,
            builder(TextureAtlas.LOCATION_BLOCKS, StateShards.NORMAL_TRANSPARENCY, LodestoneShaders.PARTICLE, NO_CULL, LIGHTMAP, COLOR_WRITE));

    public static final LodestoneRenderType TRANSPARENT_BLOCK = createGenericRenderType("transparent_block", POSITION_COLOR_TEX_LIGHTMAP, QUADS,
            builder(TextureAtlas.LOCATION_BLOCKS, StateShards.NORMAL_TRANSPARENCY, LodestoneShaders.LODESTONE_TEXTURE, NO_CULL, LIGHTMAP, COLOR_WRITE));

    public static final LodestoneRenderType TRANSPARENT_SOLID = createGenericRenderType("transparent_solid", POSITION_COLOR_LIGHTMAP, QUADS,
            builder(StateShards.NORMAL_TRANSPARENCY, POSITION_COLOR_LIGHTMAP_SHADER, NO_CULL, LIGHTMAP, COLOR_WRITE));

    public static final LodestoneRenderType LUMITRANSPARENT_PARTICLE = addUniformChanges(createCopy("lodestone:lumitransparent_particle", TRANSPARENT_PARTICLE), ShaderUniformHandler.LUMITRANSPARENT);
    public static final LodestoneRenderType LUMITRANSPARENT_BLOCK_PARTICLE = addUniformChanges(createCopy("lodestone:lumitransparent_block_particle", TRANSPARENT_BLOCK_PARTICLE), ShaderUniformHandler.LUMITRANSPARENT);
    public static final LodestoneRenderType LUMITRANSPARENT_BLOCK = addUniformChanges(createCopy("lodestone:lumitransparent_block", TRANSPARENT_BLOCK), ShaderUniformHandler.LUMITRANSPARENT);
    public static final LodestoneRenderType LUMITRANSPARENT_SOLID = addUniformChanges(createCopy("lodestone:lumitransparent_solid", TRANSPARENT_SOLID), ShaderUniformHandler.LUMITRANSPARENT);

    public static final RenderTypeProvider TEXTURE = new RenderTypeProvider((token) ->
            createGenericRenderType("texture", POSITION_COLOR_TEX_LIGHTMAP, QUADS,
                    builder(token, StateShards.NO_TRANSPARENCY, LodestoneShaders.LODESTONE_TEXTURE, CULL, LIGHTMAP)));

    public static final RenderTypeProvider CUTOUT_TEXTURE = new RenderTypeProvider((token) ->
            createGenericRenderType("cutout_texture", POSITION_COLOR_TEX_LIGHTMAP, QUADS,
                    builder(token, StateShards.NO_TRANSPARENCY, LodestoneShaders.LODESTONE_TEXTURE, CULL, LIGHTMAP)));

    public static final RenderTypeProvider TEXTURE_FADE = new RenderTypeProvider((token) ->
            createGenericRenderType("texture_fade", POSITION_COLOR_TEX_LIGHTMAP, QUADS,
                    builder(token, StateShards.NO_TRANSPARENCY, LodestoneShaders.TEXTURE_FADE, CULL, LIGHTMAP)));

    public static final RenderTypeProvider TRANSPARENT_TEXT = new RenderTypeProvider((token) ->
            LodestoneRenderTypes.createGenericRenderType("transparent_text", POSITION_COLOR_TEX_LIGHTMAP, QUADS,
                    builder(token, StateShards.NORMAL_TRANSPARENCY, LodestoneShaders.LODESTONE_TEXT, LIGHTMAP)));

    public static final RenderTypeProvider ADDITIVE_TEXT = new RenderTypeProvider((token) ->
            LodestoneRenderTypes.createGenericRenderType("additive_text", POSITION_COLOR_TEX_LIGHTMAP, QUADS,
                    builder(token, StateShards.ADDITIVE_TRANSPARENCY, LodestoneShaders.LODESTONE_TEXT, LIGHTMAP, COLOR_WRITE)));

    public static final RenderTypeProvider TRANSPARENT_TEXTURE = new RenderTypeProvider((token) ->
            createTransparentRenderType("transparent_texture", token, LodestoneShaders.LODESTONE_TEXTURE));
    public static final RenderTypeProvider TRANSPARENT_DISTORTED_TEXTURE = new RenderTypeProvider((token) ->
            createTransparentRenderType("transparent_distorted_texture", token, LodestoneShaders.DISTORTED_TEXTURE));
    public static final RenderTypeProvider TRANSPARENT_TEXTURE_TRIANGLE = new RenderTypeProvider((token) ->
            createTransparentRenderType("transparent_triangle_texture", token, LodestoneShaders.TRIANGLE_TEXTURE));
    public static final RenderTypeProvider TRANSPARENT_TWO_SIDED_TEXTURE_TRIANGLE = new RenderTypeProvider((token) ->
            createTransparentRenderType("transparent_two_sided_triangle_texture", token, LodestoneShaders.TWO_SIDED_TRIANGLE_TEXTURE));
    public static final RenderTypeProvider TRANSPARENT_ROUNDED_TEXTURE_TRIANGLE = new RenderTypeProvider((token) ->
            createTransparentRenderType("transparent_rounded_triangle_texture", token, LodestoneShaders.ROUNDED_TRIANGLE_TEXTURE));
    public static final RenderTypeProvider TRANSPARENT_SCROLLING_TEXTURE_TRIANGLE = new RenderTypeProvider((token) ->
            createTransparentRenderType("transparent_scrolling_texture_triangle", token, LodestoneShaders.ROUNDED_TRIANGLE_TEXTURE));
    public static final RenderTypeProvider TRANSPARENT_NINE_SLICE_TEXTURE = new RenderTypeProvider((token) ->
            createTransparentRenderType("transparent_nine_slice_texture", token, LodestoneShaders.NINE_SLICE));
    public static final RenderTypeProvider TRANSPARENT_DISTORTED_NINE_SLICE_TEXTURE = new RenderTypeProvider((token) ->
            createTransparentRenderType("transparent_distorted_nine_slice_texture", token, LodestoneShaders.DISTORTED_NINE_SLICE_TEXTURE));

    public static final RenderTypeProvider ADDITIVE_TEXTURE = new RenderTypeProvider((token) ->
            createAdditiveRenderType("additive_texture", token, LodestoneShaders.LODESTONE_TEXTURE));
    public static final RenderTypeProvider ADDITIVE_DISTORTED_TEXTURE = new RenderTypeProvider((token) ->
            createAdditiveRenderType("additive_distorted_texture", token, LodestoneShaders.DISTORTED_TEXTURE));
    public static final RenderTypeProvider ADDITIVE_TEXTURE_TRIANGLE = new RenderTypeProvider((token) ->
            createAdditiveRenderType("additive_triangle_texture", token, LodestoneShaders.TRIANGLE_TEXTURE));
    public static final RenderTypeProvider ADDITIVE_TWO_SIDED_TEXTURE_TRIANGLE = new RenderTypeProvider((token) ->
            createAdditiveRenderType("additive_two_sided_triangle_texture", token, LodestoneShaders.TWO_SIDED_TRIANGLE_TEXTURE));
    public static final RenderTypeProvider ADDITIVE_ROUNDED_TEXTURE_TRIANGLE = new RenderTypeProvider((token) ->
            createAdditiveRenderType("additive_rounded_triangle_texture", token, LodestoneShaders.ROUNDED_TRIANGLE_TEXTURE));
    public static final RenderTypeProvider ADDITIVE_SCROLLING_TEXTURE_TRIANGLE = new RenderTypeProvider((token) ->
            createAdditiveRenderType("additive_scrolling_texture_triangle", token, LodestoneShaders.ROUNDED_TRIANGLE_TEXTURE));
    public static final RenderTypeProvider ADDITIVE_NINE_SLICE_TEXTURE = new RenderTypeProvider((token) ->
            createAdditiveRenderType("additive_nine_slice_texture", token, LodestoneShaders.NINE_SLICE));
    public static final RenderTypeProvider ADDITIVE_DISTORTED_NINE_SLICE_TEXTURE = new RenderTypeProvider((token) ->
            createAdditiveRenderType("additive_distorted_nine_slice_texture", token, LodestoneShaders.DISTORTED_NINE_SLICE_TEXTURE));

    public static LodestoneRenderType createTransparentRenderType(String name, RenderTypeToken token, ShaderHolder shader) {
        return createGenericRenderType(name, POSITION_COLOR_TEX_LIGHTMAP, QUADS,
                builder(token, StateShards.NORMAL_TRANSPARENCY, shader, CULL, LIGHTMAP, COLOR_WRITE));
    }
    public static LodestoneRenderType createAdditiveRenderType(String name, RenderTypeToken token, ShaderHolder shader) {
        return createGenericRenderType(name, POSITION_COLOR_TEX_LIGHTMAP, QUADS,
                builder(token, StateShards.ADDITIVE_TRANSPARENCY, shader, CULL, LIGHTMAP, COLOR_WRITE));
    }

    public static LodestoneRenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.Mode mode, LodestoneCompositeStateBuilder builder) {
        return createGenericRenderType(name, format, mode, builder, null);
    }

    public static LodestoneRenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.Mode mode, LodestoneCompositeStateBuilder builder, ShaderUniformHandler handler) {
        if (MODIFIER != null) {
            MODIFIER.accept(builder);
        }
        LodestoneRenderType type = new LodestoneRenderType(name, format, builder.modeOverride != null ? builder.modeOverride : mode, 256, false, true, builder.createCompositeState(true));
        RenderHandler.addRenderType(type);
        if (handler != null) {
            addUniformChanges(type, handler);
        }
        MODIFIER = null;
        return type;
    }

    public static LodestoneRenderType createCopy(LodestoneRenderTypeBuilder type) {
        return createCopy(type.getRenderType());
    }

    public static LodestoneRenderType createCopy(String newName, LodestoneRenderTypeBuilder type) {
        return createCopy(newName, type.getRenderType());
    }

    public static LodestoneRenderType createCopy(LodestoneRenderType type) {
        return GENERIC.apply(new RenderTypeData(type));
    }

    public static LodestoneRenderType createCopy(String newName, LodestoneRenderType type) {
        return GENERIC.apply(new RenderTypeData(newName, type));
    }

    public static LodestoneRenderType createCachedCopy(Object index, LodestoneRenderTypeBuilder type) {
        return createCachedCopy(index, type.getRenderType());
    }

    public static LodestoneRenderType createCachedCopy(Object index, LodestoneRenderType type) {
        return COPIES.computeIfAbsent(Pair.of(index, type), (p) -> GENERIC.apply(new RenderTypeData(type)));
    }

    public static LodestoneRenderType addUniformChanges(LodestoneRenderType type, ShaderUniformHandler handler) {
        RenderHandler.UNIFORM_HANDLERS.put(type, handler);
        return type;
    }

    public static void addRenderTypeModifier(Consumer<LodestoneCompositeStateBuilder> modifier) {
        MODIFIER = modifier;
    }

    public static LodestoneCompositeStateBuilder builder(Object... objects) {
        return builder().setStateShards(objects);
    }
    public static LodestoneCompositeStateBuilder builder() {
        return new LodestoneCompositeStateBuilder();
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class LodestoneCompositeStateBuilder extends RenderType.CompositeState.CompositeStateBuilder {

        protected VertexFormat.Mode modeOverride;
        LodestoneCompositeStateBuilder() {
            super();
        }

        public LodestoneCompositeStateBuilder replaceVertexFormat(VertexFormat.Mode mode) {
            this.modeOverride = mode;
            return this;
        }

        public LodestoneCompositeStateBuilder copyState(RenderType.CompositeState state) {
            for (RenderStateShard renderStateShard : state.states) {
                setStateShards(renderStateShard);
            }
            return this;
        }

        public LodestoneCompositeStateBuilder setStateShards(Object... objects) {
            for (Object object : objects) {
                if (object instanceof ResourceLocation texture) {
                    setTextureState(texture);
                }
                else if (object instanceof RenderTypeToken token) {
                    setTextureState(token);
                }
                else if (object instanceof RenderStateShard.EmptyTextureStateShard shard) {
                    setTextureState(shard);
                }
                else if (object instanceof ShaderHolder shaderHolder) {
                    setShaderState(shaderHolder);
                }
                else if (object instanceof RenderStateShard.ShaderStateShard shard) {
                    setShaderState(shard);
                }
                else if (object instanceof RenderStateShard.TransparencyStateShard shard) {
                    setTransparencyState(shard);
                }
                else if (object instanceof RenderStateShard.DepthTestStateShard shard) {
                    setDepthTestState(shard);
                }
                else if (object instanceof RenderStateShard.CullStateShard shard) {
                    setCullState(shard);
                }
                else if (object instanceof RenderStateShard.LightmapStateShard shard) {
                    setLightmapState(shard);
                }
                else if (object instanceof RenderStateShard.OverlayStateShard shard) {
                    setOverlayState(shard);
                }
                else if (object instanceof RenderStateShard.LayeringStateShard shard) {
                    setLayeringState(shard);
                }
                else if (object instanceof RenderStateShard.OutputStateShard shard) {
                    setOutputState(shard);
                }
                else if (object instanceof RenderStateShard.TexturingStateShard shard) {
                    setTexturingState(shard);
                }
                else if (object instanceof RenderStateShard.WriteMaskStateShard shard) {
                    setWriteMaskState(shard);
                }
                else if (object instanceof RenderStateShard.LineStateShard shard) {
                    setLineState(shard);
                }
                else if (object instanceof RenderStateShard.ColorLogicStateShard shard) {
                    setColorLogicState(shard);
                }
            }
            return this;
        }

        public LodestoneCompositeStateBuilder setTextureState(RenderTypeToken token) {
            return setTextureState(token.getTexture());
        }

        public LodestoneCompositeStateBuilder setTextureState(ResourceLocation texture) {
            return setTextureState(new RenderStateShard.TextureStateShard(texture, false, false));
        }

        public LodestoneCompositeStateBuilder setShaderState(ShaderHolder shaderHolder) {
            return setShaderState(shaderHolder.getShard());
        }

        @Override
        public LodestoneCompositeStateBuilder setTextureState(EmptyTextureStateShard pTextureState) {
            return (LodestoneCompositeStateBuilder) super.setTextureState(pTextureState);
        }

        @Override
        public LodestoneCompositeStateBuilder setShaderState(ShaderStateShard pShaderState) {
            return (LodestoneCompositeStateBuilder) super.setShaderState(pShaderState);
        }

        @Override
        public LodestoneCompositeStateBuilder setTransparencyState(TransparencyStateShard pTransparencyState) {
            return (LodestoneCompositeStateBuilder) super.setTransparencyState(pTransparencyState);
        }

        @Override
        public LodestoneCompositeStateBuilder setDepthTestState(DepthTestStateShard pDepthTestState) {
            return (LodestoneCompositeStateBuilder) super.setDepthTestState(pDepthTestState);
        }

        @Override
        public LodestoneCompositeStateBuilder setCullState(CullStateShard pCullState) {
            return (LodestoneCompositeStateBuilder) super.setCullState(pCullState);
        }

        @Override
        public LodestoneCompositeStateBuilder setLightmapState(LightmapStateShard pLightmapState) {
            return (LodestoneCompositeStateBuilder) super.setLightmapState(pLightmapState);
        }

        @Override
        public LodestoneCompositeStateBuilder setOverlayState(OverlayStateShard pOverlayState) {
            return (LodestoneCompositeStateBuilder) super.setOverlayState(pOverlayState);
        }

        @Override
        public LodestoneCompositeStateBuilder setLayeringState(LayeringStateShard pLayerState) {
            return (LodestoneCompositeStateBuilder) super.setLayeringState(pLayerState);
        }

        @Override
        public LodestoneCompositeStateBuilder setOutputState(OutputStateShard pOutputState) {
            return (LodestoneCompositeStateBuilder) super.setOutputState(pOutputState);
        }

        @Override
        public LodestoneCompositeStateBuilder setTexturingState(TexturingStateShard pTexturingState) {
            return (LodestoneCompositeStateBuilder) super.setTexturingState(pTexturingState);
        }

        @Override
        public LodestoneCompositeStateBuilder setWriteMaskState(WriteMaskStateShard pWriteMaskState) {
            return (LodestoneCompositeStateBuilder) super.setWriteMaskState(pWriteMaskState);
        }

        @Override
        public LodestoneCompositeStateBuilder setLineState(LineStateShard pLineState) {
            return (LodestoneCompositeStateBuilder) super.setLineState(pLineState);
        }
    }
}