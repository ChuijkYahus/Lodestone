package team.lodestar.lodestone.systems.model.geo.data;

import org.joml.Vector3f;

public record GeoDescription(String identifier, int textureWidth, int textureHeight, float visibleBoundsWidth, float visibleBoundsHeight, Vector3f visibleBoundsOffset) {
}
