package team.lodestar.lodestone.registry.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

public class LodestoneVertexFormats {
    public static VertexFormatElement TANGENT = VertexFormatElement.register(VertexFormatElement.findNextId(), 0, VertexFormatElement.Type.BYTE, VertexFormatElement.Usage.NORMAL, 3);
    public static VertexFormatElement BITANGENT = VertexFormatElement.register(VertexFormatElement.findNextId(), 0, VertexFormatElement.Type.BYTE, VertexFormatElement.Usage.NORMAL, 3);
}