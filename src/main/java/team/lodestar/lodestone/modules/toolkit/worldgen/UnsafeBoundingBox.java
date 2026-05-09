package team.lodestar.lodestone.modules.toolkit.worldgen;

import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class UnsafeBoundingBox {

    protected int minX;
    protected int minY;
    protected int minZ;
    protected int maxX;
    protected int maxY;
    protected int maxZ;

    public void encapsulate(Vec3i pos) {
        if (minX > pos.getX()) {
            minX = pos.getX();
        }
        if (minY > pos.getY()) {
            minY = pos.getY();
        }
        if (minZ > pos.getZ()) {
            minZ = pos.getZ();
        }
        if (maxX < pos.getX()) {
            maxX = pos.getX();
        }
        if (maxY < pos.getY()) {
            maxY = pos.getY();
        }
        if (maxZ < pos.getZ()) {
            maxZ = pos.getZ();
        }
    }

    public BoundingBox toBoundingBox() {
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
}