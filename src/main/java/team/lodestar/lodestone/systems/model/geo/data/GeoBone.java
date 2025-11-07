package team.lodestar.lodestone.systems.model.geo.data;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

public class GeoBone {
    private Vector3f position;
    private float xRot;
    private float yRot;
    private float zRot;
    private Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);
    private final List<GeoCube> cubes;
    private final Map<String, GeoBone> children;
    private final String parent;

    public GeoBone(List<GeoCube> cubes, Map<String, GeoBone> children, String parent) {
        this.cubes = cubes;
        this.children = children;
        this.parent = parent;
    }

    public void copyFrom(GeoBone geoBone) {
        this.scale.x = geoBone.scale.x;
        this.scale.y = geoBone.scale.y;
        this.scale.z = geoBone.scale.z;
        this.xRot = geoBone.xRot;
        this.yRot = geoBone.yRot;
        this.zRot = geoBone.zRot;
        this.position.x = geoBone.position.x;
        this.position.y = geoBone.position.y;
        this.position.z = geoBone.position.z;
    }

    public boolean hasChild(String name) {
        return this.children.containsKey(name);
    }

    public GeoBone getChild(String name) {
        GeoBone bone = this.children.get(name);
        if (bone == null) {
            throw new NoSuchElementException("Can't find bone " + name);
        } else {
            return bone;
        }
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    public void setRotation(float xDeg, float yDeg, float zDeg) {
        this.xRot = (float)Math.toRadians(xDeg);
        this.yRot = (float)Math.toRadians(yDeg);
        this.zRot = (float)Math.toRadians(zDeg);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getxRot() {
        return xRot;
    }

    public float getyRot() {
        return yRot;
    }

    public float getzRot() {
        return zRot;
    }

    public Vector3f getScale() {
        return scale;
    }

    public List<GeoCube> getCubes() {
        return cubes;
    }

    public Map<String, GeoBone> getChildren() {
        return children;
    }

    public String getParent() {
        return parent;
    }

    public void addChild(String name, GeoBone bone) {
        this.children.put(name, bone);
    }

    public void render(PoseStack poseStack, VertexConsumer vertexConsumer, RenderType renderType) {
        if (!this.cubes.isEmpty() || !this.children.isEmpty()) {
            poseStack.pushPose();
            this.translateAndRotate(poseStack);
            for (GeoCube cube : this.cubes) {
                cube.render(poseStack, vertexConsumer, renderType);
            }

            for (GeoBone child : this.children.values()) {
                child.render(poseStack, vertexConsumer, renderType);
            }
            poseStack.popPose();
        }
    }

    public void translateAndRotate(PoseStack poseStack) {
        float px = this.position.x / 16.0F;
        float py = this.position.y / 16.0F;
        float pz = this.position.z / 16.0F;

        poseStack.translate(px, py, pz);

        if (this.xRot != 0.0F || this.yRot != 0.0F || this.zRot != 0.0F) {
            poseStack.mulPose(new Quaternionf().rotationZYX(-this.zRot, this.yRot, -this.xRot));
        }

        poseStack.translate(-px, -py, -pz);

        if (this.scale.x != 1.0F || this.scale.y != 1.0F || this.scale.z != 1.0F) {
            poseStack.scale(this.scale.x, this.scale.y, this.scale.z);
        }
    }

}
