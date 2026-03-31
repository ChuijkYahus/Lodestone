package team.lodestar.lodestone.modules.toolkit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntity;
import team.lodestar.lodestone.modules.toolkit.inventory.ItemStackHandlerItemDisplayData;
import team.lodestar.lodestone.modules.toolkit.inventory.LodestoneItemStackBlockHandler;
import team.lodestar.lodestone.modules.toolkit.inventory.LodestoneItemStackHandler;

import java.util.Optional;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class ItemStackDisplayDataRenderer {

    protected float itemScale = 0.5f;
    protected float rotationRate = 2f;

    public ItemStackDisplayDataRenderer() {

    }

    public ItemStackDisplayDataRenderer setItemScale(float itemScale) {
        this.itemScale = itemScale;
        return this;
    }

    public ItemStackDisplayDataRenderer setRotationRate(float rotationRate) {
        this.rotationRate = rotationRate;
        return this;
    }

    public void render(LodestoneItemStackBlockHandler handler, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, float partialTicks) {
        var level = Minecraft.getInstance().level;
        var itemRenderer = Minecraft.getInstance().getItemRenderer();
        var parent = handler.getParent();
        var pos = parent.getBlockPos();
        float time = level.getGameTime() + partialTicks;

        var displayData = handler.getDisplayData();
        assert displayData != null;
        var stacks = handler.getStacks();

        for (int i = 0; i < stacks.size(); i++) {
            var optional = displayData.getEntry(i);
            if (optional.isEmpty()) {
                continue;
            }
            var entry = optional.get();
            var stack = entry.getStack();
            var angle = entry.getAngle(partialTicks);
            var distance = entry.getDistance(partialTicks);
            var lift = entry.getLift(partialTicks);
            var center = displayData.getDisplayCenter(partialTicks);

            var xt = center.x - pos.getX() + Mth.sin(angle) * distance;
            var yt = center.y - pos.getY() + lift;
            var zt = center.z - pos.getZ() + Mth.cos(angle) * distance;
            poseStack.pushPose();
            poseStack.translate(xt, yt, zt);
            poseStack.mulPose(Axis.YP.rotationDegrees((time * rotationRate) % 360));
            poseStack.scale(itemScale, itemScale, itemScale);
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, combinedLightIn, NO_OVERLAY, poseStack, bufferIn, level, 0);
            poseStack.popPose();
        }
    }
}
