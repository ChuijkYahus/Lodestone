package team.lodestar.lodestone.systems.model.geo.data;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;

public interface IRenderableModelPart<T> extends ICloneable<T> {
    void render(PoseStack poseStack, VertexConsumer vertexConsumer, RenderType renderType);
}
