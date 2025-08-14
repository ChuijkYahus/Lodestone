package team.lodestar.lodestone.systems.rendering.vertexconsumer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link VertexConsumer} that forwards all calls to multiple other {@link VertexConsumer}s.
 * <p>Useful for rendering the same geometry to multiple buffers.</p>
 */
@SuppressWarnings("unused")
public class MultiVertexConsumer implements VertexConsumer {
    private final VertexConsumer[] consumers;

    public MultiVertexConsumer(VertexConsumer... consumers) {
        this.consumers = consumers;
    }

    @Override
    public @NotNull VertexConsumer addVertex(float x, float y, float z) {
        for (VertexConsumer consumer : consumers) {
            consumer.addVertex(x, y, z);
        }
        return this;
    }

    @Override
    public @NotNull VertexConsumer setColor(int red, int green, int blue, int alpha) {
        for (VertexConsumer consumer : consumers) {
            consumer.setColor(red, green, blue, alpha);
        }
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv(float u, float v) {
        for (VertexConsumer consumer : consumers) {
            consumer.setUv(u, v);
        }
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv1(int u, int v) {
        for (VertexConsumer consumer : consumers) {
            consumer.setUv1(u, v);
        }
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv2(int u, int v) {
        for (VertexConsumer consumer : consumers) {
            consumer.setUv2(u, v);
        }
        return this;
    }

    @Override
    public @NotNull VertexConsumer setNormal(float normalX, float normalY, float normalZ) {
        for (VertexConsumer consumer : consumers) {
            consumer.setNormal(normalX, normalY, normalZ);
        }
        return this;
    }
}
