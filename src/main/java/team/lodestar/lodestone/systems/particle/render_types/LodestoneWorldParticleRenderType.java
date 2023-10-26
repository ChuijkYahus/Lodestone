package team.lodestar.lodestone.systems.particle.render_types;

import net.minecraft.client.renderer.*;
import team.lodestar.lodestone.setup.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.setup.LodestoneShaderRegistry;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import org.lwjgl.opengl.GL11;

public interface LodestoneWorldParticleRenderType extends ParticleRenderType {

    //TODO: this class needs a refactor, there's barely any difference between teh different render types.


    LodestoneWorldParticleRenderType ADDITIVE = new LodestoneWorldParticleRenderType() {
        @Override
        public RenderType getRenderType() {
            return LodestoneRenderTypeRegistry.ADDITIVE_PARTICLE;
        }

        @Override
        public void begin(BufferBuilder builder, TextureManager manager) {
            RenderSystem.depthMask(true);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            RenderSystem.setShader(LodestoneShaderRegistry.PARTICLE.getInstance());
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    };
    LodestoneWorldParticleRenderType TRANSPARENT = new LodestoneWorldParticleRenderType() {
        @Override
        public RenderType getRenderType() {
            return LodestoneRenderTypeRegistry.TRANSPARENT_PARTICLE;
        }

        @Override
        public void begin(BufferBuilder builder, TextureManager manager) {
            RenderSystem.depthMask(true);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShader(LodestoneShaderRegistry.PARTICLE.getInstance());
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    };
    LodestoneWorldParticleRenderType LUMITRANSPARENT = new LodestoneWorldParticleRenderType() {
        @Override
        public RenderType getRenderType() {
            return LodestoneRenderTypeRegistry.LUMITRANSPARENT_PARTICLE;
        }

        @Override
        public void begin(BufferBuilder builder, TextureManager manager) {
            RenderSystem.depthMask(true);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShader(LodestoneShaderRegistry.PARTICLE.getInstance());
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    };
    LodestoneWorldParticleRenderType TERRAIN_SHEET = new LodestoneWorldParticleRenderType() {
        @Override
        public RenderType getRenderType() {
            return LodestoneRenderTypeRegistry.TRANSPARENT_BLOCK_PARTICLE;
        }

        @Override
        public void begin(BufferBuilder builder, TextureManager manager) {
            RenderSystem.depthMask(true);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShader(LodestoneShaderRegistry.PARTICLE.getInstance());
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    };

    default boolean shouldBuffer() {
        return true;
    }

    RenderType getRenderType();
}