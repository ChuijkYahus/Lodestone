package team.lodestar.lodestone.systems.postprocess.effects;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;
import team.lodestar.lodestone.systems.texture.CustomizableTextureTarget;
import team.lodestar.lodestone.systems.texture.InternalTextureFormat;

public class PhysicallyBasedBloomPostProcessor extends PostProcessor {
    private RenderTarget bloomTarget;
    private final RenderStateShard.OutputStateShard bloomOutput;

    private RenderTarget BLURX2, BLURY2, BLURX4, BLURY4, BLURX8, BLURY8;

    private boolean forceDisabled;

    public PhysicallyBasedBloomPostProcessor() {
        this.bloomOutput = new RenderStateShard.OutputStateShard("bloomTarget",
                () -> { if (this.bloomTarget != null) this.bloomTarget.bindWrite(false);},
                () -> Minecraft.getInstance().getMainRenderTarget().bindWrite(false)
        );
        this.setActive(false);
    }
    @Override
    public ResourceLocation getPostChainLocation() {
        return LodestoneLib.lodestonePath("pb_bloom");
    }

    @Override
    public void init() {
        super.init();
        if (this.postChain != null) {
            this.bloomTarget = this.postChain.getTempTarget("bloomColor");

            this.BLURX2 = this.postChain.getTempTarget("blurX2");
            this.BLURY2 = this.postChain.getTempTarget("blurY2");
            this.BLURX4 = this.postChain.getTempTarget("blurX4");
            this.BLURY4 = this.postChain.getTempTarget("blurY4");
            this.BLURX8 = this.postChain.getTempTarget("blurX8");
            this.BLURY8 = this.postChain.getTempTarget("blurY8");
            var window = Minecraft.getInstance().getWindow();
            this.resize(window.getWidth(), window.getHeight());
        }
    }

    @Override
    public void beforeProcess(Matrix4f viewModelMatrix) {
    }

    @Override
    public void afterProcess() {
        if (this.bloomTarget == null) return;
        this.bloomTarget.clear(Minecraft.ON_OSX);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        //this.bloomTarget.resize(width, height, Minecraft.ON_OSX);

        this.BLURX2.resize(width / 2, height / 2, Minecraft.ON_OSX);
        this.BLURY2.resize(width / 2, height / 2, Minecraft.ON_OSX);
        this.BLURX4.resize(width / 4, height / 4, Minecraft.ON_OSX);
        this.BLURY4.resize(width / 4, height / 4, Minecraft.ON_OSX);
        this.BLURX8.resize(width / 8, height / 8, Minecraft.ON_OSX);
        this.BLURY8.resize(width / 8, height / 8, Minecraft.ON_OSX);

        this.BLURX2.setFilterMode(GL11.GL_LINEAR);
        this.BLURY2.setFilterMode(GL11.GL_LINEAR);
        this.BLURX4.setFilterMode(GL11.GL_LINEAR);
        this.BLURY4.setFilterMode(GL11.GL_LINEAR);
        this.BLURX8.setFilterMode(GL11.GL_LINEAR);
        this.BLURY8.setFilterMode(GL11.GL_LINEAR);
    }

    public void forceDisable() {
        this.forceDisabled = true;
        this.setActive(false);
    }

    @Override
    public void setActive(boolean active) {
        if (this.forceDisabled) active = false;
        super.setActive(active);
    }

    public RenderStateShard.OutputStateShard getBloomOutput() {
        return bloomOutput;
    }

    public RenderTarget getBloomTarget() {
        return bloomTarget;
    }

    public void copyDepthFromMain() {
        this.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
    }

    public void copyDepthFrom(RenderTarget src) {
        if (this.bloomTarget == null || src == null) return;
        this.bloomTarget.copyDepthFrom(src);
        GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, src.frameBufferId);
    }
}
