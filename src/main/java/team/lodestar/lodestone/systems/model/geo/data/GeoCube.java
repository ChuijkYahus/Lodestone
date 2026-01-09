package team.lodestar.lodestone.systems.model.geo.data;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import org.joml.Vector3f;

public class GeoCube {
    private final Vector3f origin;
    private final Vector3f size;
    private final Vector3f rotation;
    private final GeoQuad[] quads;

    public GeoCube(GeoQuad[] quads, Vector3f origin, Vector3f size, Vector3f rotation) {
        this.origin = new Vector3f(origin);
        this.size   = new Vector3f(size);
        this.rotation = new Vector3f(rotation);
        this.quads  = quads;
    }

    public void render(PoseStack poseStack, VertexConsumer vc, RenderType rt) {
        for (GeoQuad quad : quads) {
            for (GeoVertex vertex : quad.vertices) {
                Vector3f pos = new Vector3f(vertex.getPosition());
                vc.addVertex(poseStack.last().pose(), pos.x, pos.y, pos.z)
                        .setUv(vertex.getTexCoord().x, vertex.getTexCoord().y)
                        .setColor(255, 255, 255, 255);
            }
        }
    }

    /**
     * Creates a complete copy of this GeoCube.
     * @return The copied GeoCube.
     */
    public GeoCube copy() {
        GeoQuad[] copiedQuads = new GeoQuad[this.quads.length];
        for (int i = 0; i < this.quads.length; i++) {
            copiedQuads[i] = this.quads[i].copy();
        }
        return new GeoCube(copiedQuads, new Vector3f(this.origin), new Vector3f(this.size), new Vector3f(this.rotation));
    }
}
