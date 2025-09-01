package team.lodestar.lodestone.registry.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

public class LodestoneVertexFormats {
    public static VertexFormatElement TANGENT = VertexFormatElement.register(VertexFormatElement.findNextId(), 0, VertexFormatElement.Type.BYTE, VertexFormatElement.Usage.NORMAL, 3);
    public static VertexFormatElement BITANGENT = VertexFormatElement.register(VertexFormatElement.findNextId(), 0, VertexFormatElement.Type.BYTE, VertexFormatElement.Usage.NORMAL, 3);
    public static VertexFormatElement SIZE2 = VertexFormatElement.register(VertexFormatElement.findNextId(), 0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.POSITION, 2);

    public static VertexFormat POSITION_TEX_SIZE2 = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("UV0", VertexFormatElement.UV0)
            .add("Size", SIZE2)
            .build();
}