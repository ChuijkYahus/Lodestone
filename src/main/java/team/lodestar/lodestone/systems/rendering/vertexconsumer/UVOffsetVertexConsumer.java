package team.lodestar.lodestone.systems.rendering.vertexconsumer;

import com.mojang.blaze3d.vertex.*;
import org.jetbrains.annotations.*;

/**
 * A {@link VertexConsumer} that forwards all calls to multiple other {@link VertexConsumer}s.
 * <p>Useful for rendering the same geometry to multiple buffers.</p>
 * This implementation also allows for an offset to be applied to the UV coordinates
 */
@SuppressWarnings("unused")
public class UVOffsetVertexConsumer extends MultiVertexConsumer {

    protected float uOffset;
    protected float vOffset;

    public UVOffsetVertexConsumer(VertexConsumer... consumers) {
        super(consumers);
    }

    public UVOffsetVertexConsumer setOffset(float uOffset, float vOffset) {
        this.uOffset = uOffset;
        this.vOffset = vOffset;
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv(float u, float v) {
        return super.setUv(u+uOffset, v+vOffset);
    }
}
