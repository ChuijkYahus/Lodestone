package team.lodestar.lodestone.modules.rendering.particle.visual.types.mesh;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import net.minecraft.client.renderer.RenderType;
import team.lodestar.lodestone.modules.rendering.particle.visual.instance.InstanceFormat;
import team.lodestar.lodestone.systems.model.IRenderableModel;

import java.util.Objects;

public class MeshVisualConfig {
    protected RenderType renderType;
    protected InstanceFormat instanceFormat;
    protected VertexBuffer vertexBuffer;
    protected float offsetX = 0.0f;
    protected float offsetY = 0.0f;
    protected float offsetZ = 0.0f;

    public MeshVisualConfig renderType(RenderType renderType) {
        this.renderType = renderType;
        return this;
    }

    public MeshVisualConfig instanceFormat(InstanceFormat instanceFormat) {
        this.instanceFormat = instanceFormat;
        return this;
    }

    public MeshVisualConfig instancedModel(IRenderableModel model) {
        if (renderType == null) {
            throw new IllegalArgumentException("Render type is null in config: " + this);
        }
        this.vertexBuffer = model.createModelBuffer(new PoseStack(), renderType);
        return this;
    }

    public MeshVisualConfig vertexBuffer(VertexBuffer vertexBuffer) {
        this.vertexBuffer = vertexBuffer;
        return this;
    }

    public MeshVisualConfig offset(float x, float y, float z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeshVisualConfig that)) return false;
        return Float.compare(that.offsetX, offsetX) == 0 &&
                Float.compare(that.offsetY, offsetY) == 0 &&
                Float.compare(that.offsetZ, offsetZ) == 0 &&
                Objects.equals(renderType, that.renderType) &&
                Objects.equals(instanceFormat, that.instanceFormat) &&
                Objects.equals(vertexBuffer, that.vertexBuffer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(renderType, instanceFormat, vertexBuffer, offsetX, offsetY, offsetZ);
    }
}