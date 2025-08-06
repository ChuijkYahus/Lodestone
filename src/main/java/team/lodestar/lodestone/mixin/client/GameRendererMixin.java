package team.lodestar.lodestone.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderSystem;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "resize", at = @At(value = "HEAD"))
    public void lodestone$injectionResizeListener(int width, int height, CallbackInfo ci) {
        LodestoneRenderHandler.resize(width, height);
        PostProcessHandler.resize(width, height);
    }

    @ModifyArgs(method = "bobView", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    public void extractViewBob(Args args) {
        LodestoneRenderSystem.setViewBobOffset(-(float) args.get(0), -(float) args.get(1), -(float) args.get(2));
    }

    @Inject(method = "renderLevel", at = @At("HEAD"))
    public void clearViewBob(CallbackInfo ci) {
        if (!Minecraft.getInstance().options.bobView().get()) {
            LodestoneRenderSystem.setViewBobOffset(0, 0, 0);
        }
    }
}
