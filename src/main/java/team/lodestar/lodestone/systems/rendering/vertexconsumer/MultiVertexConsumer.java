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
    public @NotNull VertexConsumer addVertex(float v, float v1, float v2) {
        for (VertexConsumer consumer : consumers) {
            consumer.addVertex(v, v1, v2);
        }
        return this;
    }

    @Override
    public @NotNull VertexConsumer setColor(int i, int i1, int i2, int i3) {
        for (VertexConsumer consumer : consumers) {
            consumer.setColor(i, i1, i2, i3);
        }
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv(float v, float v1) {
        for (VertexConsumer consumer : consumers) {
            consumer.setUv(v, v1);
        }
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv1(int i, int i1) {
        for (VertexConsumer consumer : consumers) {
            consumer.setUv1(i, i1);
        }
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv2(int i, int i1) {
        for (VertexConsumer consumer : consumers) {
            consumer.setUv2(i, i1);
        }
        return this;
    }

    @Override
    public @NotNull VertexConsumer setNormal(float v, float v1, float v2) {
        for (VertexConsumer consumer : consumers) {
            consumer.setNormal(v, v1, v2);
        }
        return this;
    }
}
