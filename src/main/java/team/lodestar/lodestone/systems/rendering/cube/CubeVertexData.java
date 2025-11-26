package team.lodestar.lodestone.systems.rendering.cube;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record CubeVertexData(Vector3f[] bottomVertices, Vector3f[] topVertices, List<Vector3f[]> offsetMap) {

    public static CubeVertexData makeCubePositions(float scale) {
        return makeCubePositions(scale, scale);
    }

    public static CubeVertexData makeCubePositions(float hScale, float vScale) {
        float xOffset = hScale / 2f;
        float yOffset = vScale / 2f;
        return makeCubePositions(-xOffset, xOffset, -yOffset, yOffset);
    }

    public static CubeVertexData makeCubePositions(float hStart, float hEnd, float vStart, float vEnd) {
        return makeCubePositions(hStart, hEnd, vStart, vEnd, hStart, hEnd);
    }

    public static CubeVertexData makeCubePositions(float xStart, float xEnd, float yStart, float yEnd, float zStart, float zEnd) {
        Vector3f[] bottomVertices = new Vector3f[]{new Vector3f(xStart, yStart, zStart), new Vector3f(xStart, yStart, zEnd), new Vector3f(xEnd, yStart, zEnd), new Vector3f(xEnd, yStart, zStart)};
        Vector3f[] topVertices = new Vector3f[]{new Vector3f(xStart, yEnd, zStart), new Vector3f(xStart, yEnd, zEnd), new Vector3f(xEnd, yEnd, zEnd), new Vector3f(xEnd, yEnd, zStart)};
        List<Vector3f[]> offsetMap = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int index = (i*3+3) % 4; //this weird and specific numbering is to tie the vertices to horizontal directions
            offsetMap.add(new Vector3f[]{bottomVertices[(index) % 4], bottomVertices[(index+1) % 4], topVertices[(index+1) % 4], topVertices[(index) % 4]});
        }
        return new CubeVertexData(bottomVertices, topVertices, offsetMap);
    }

    public Vector3f[] getVerticesByDirection(Direction direction) {
        if (direction.equals(Direction.UP)) {
            return topVertices;
        }
        if (direction.equals(Direction.DOWN)) {
            return new Vector3f[]{bottomVertices[3], bottomVertices[2], bottomVertices[1], bottomVertices[0]};
        }
        return getVerticesByIndex(direction.get2DDataValue());
    }

    public Vector3f[] getVerticesByIndex(int index) {
        return offsetMap.get(index);
    }

    public CubeVertexData applyWobble(float sineOffset, float strength) {
        return applyWobble(sineOffset, sineOffset, strength);
    }

    public CubeVertexData applyWobble(float bottomSineOffset, float topSineOffset, float strength) {
        return applyWobble(bottomVertices, bottomSineOffset, strength).applyWobble(topVertices, topSineOffset, strength);
    }

    public CubeVertexData applyWobble(Vector3f[] offsets, float sineOffset, float strength) {
        applyVertexWobble(offsets, sineOffset, strength);
        return this;
    }

    public static void applyVertexWobble(Vector3f[] offsets, float sineOffset, float strength) {
        float offset = sineOffset;
        for (Vector3f vector3f : offsets) {
            double time = ((Minecraft.getInstance().level.getGameTime() / 40.0F) % 6.28f);
            float angle = (float) (time + (offset * 6.28f));
            float sin = Mth.sin(angle) * strength;
            float cos = Mth.cos(angle) * strength;
            vector3f.add(sin, cos, 0);
            offset += 0.25f;
        }
    }

    public CubeVertexData scale(float scale) {
        return scale(scale, scale);
    }

    public CubeVertexData scale(float width, float height) {
        return scale(width, height, width);
    }

    public CubeVertexData scale(float x, float y, float z) {
        for (int i = 0; i < bottomVertices.length; i++) {
            bottomVertices[i].mul(x, y, z);
            topVertices[i].mul(x, y, z);
        }
        return this;
    }

    public CubeVertexData offset(float x, float y, float z) {
        for (int i = 0; i < bottomVertices.length; i++) {
            bottomVertices[i].add(x, y, z);
            topVertices[i].add(x, y, z);
        }
        return this;
    }
}