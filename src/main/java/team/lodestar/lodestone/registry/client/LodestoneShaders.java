package team.lodestar.lodestone.registry.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.rendering.shader.LodestoneShader;
import team.lodestar.lodestone.systems.rendering.shader.LodestoneShaderRegistry;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;
import team.lodestar.lodestone.systems.rendering.shader.compute.ComputeProgram;

import java.util.ArrayList;
import java.util.List;

import static team.lodestar.lodestone.LodestoneLib.lodestonePath;

@EventBusSubscriber(value = Dist.CLIENT, modid = LodestoneLib.LODESTONE, bus = EventBusSubscriber.Bus.MOD)
public class LodestoneShaders {
    private static final LodestoneShaderRegistry SHADERS = new LodestoneShaderRegistry(LodestoneLib.LODESTONE);

    public static ShaderHolder LODESTONE_TEXTURE = SHADERS.register(new ShaderHolder(lodestonePath("lodestone_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));
    public static ShaderHolder DISTORTED_TEXTURE = SHADERS.register(new ShaderHolder(lodestonePath("distorted_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));
    public static ShaderHolder TEXTURE_FADE = SHADERS.register(new ShaderHolder(lodestonePath("texture_fade"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));
    public static ShaderHolder LODESTONE_TEXT = SHADERS.register(new ShaderHolder(lodestonePath("lodestone_text"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));

    public static ShaderHolder PARTICLE = SHADERS.register(new ShaderHolder(lodestonePath("particle/lodestone_particle"), DefaultVertexFormat.PARTICLE));

    public static ShaderHolder SCREEN_PARTICLE = SHADERS.register(new ShaderHolder(lodestonePath("screen/screen_particle"), DefaultVertexFormat.POSITION_TEX_COLOR));
    public static ShaderHolder SCREEN_DISTORTED_TEXTURE = SHADERS.register(new ShaderHolder(lodestonePath("screen/screen_distorted_texture"), DefaultVertexFormat.POSITION_TEX_COLOR));

    public static ShaderHolder TRIANGLE_TEXTURE = SHADERS.register(new ShaderHolder(lodestonePath("shapes/triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));
    public static ShaderHolder TWO_SIDED_TRIANGLE_TEXTURE = SHADERS.register(new ShaderHolder(lodestonePath("shapes/two_sided_triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));
    public static ShaderHolder ROUNDED_TRIANGLE_TEXTURE = SHADERS.register(new ShaderHolder(lodestonePath("shapes/rounded_triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));

    public static ShaderHolder SCROLLING_TEXTURE = SHADERS.register(new ShaderHolder(lodestonePath("shapes/scrolling_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));
    public static ShaderHolder SCROLLING_TRIANGLE_TEXTURE = SHADERS.register(new ShaderHolder(lodestonePath("shapes/scrolling_triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));

    public static ShaderHolder NINE_SLICE = SHADERS.register(new ShaderHolder(lodestonePath("nineslice/nine_slice"), DefaultVertexFormat.POSITION_TEX));
    public static ShaderHolder DISTORTED_NINE_SLICE_TEXTURE = SHADERS.register(new ShaderHolder(lodestonePath("nineslice/distorted/distorted_nine_slice_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));
    public static ShaderHolder MANUAL_NINE_SLICE = SHADERS.register(new ShaderHolder(lodestonePath("nineslice/gui/nine_slice"), LodestoneVertexFormats.POSITION_TEX_SIZE2));

    public static ShaderHolder RADIAL_DISTORTED_SCREEN_LIGHT = SHADERS.register(new ShaderHolder(lodestonePath("screen/radial_distorted_light"), DefaultVertexFormat.POSITION_TEX_COLOR));

    // Compute
    public static ComputeProgram TEST = SHADERS.register(new ComputeProgram(LodestoneLib.lodestonePath("sdf")));

    // Debug
    public static ShaderHolder DEBUG_SDF = SHADERS.register(new ShaderHolder(LodestoneLib.lodestonePath("debug/sdf"), DefaultVertexFormat.POSITION));

    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) {
        SHADERS.init(event);
    }
}