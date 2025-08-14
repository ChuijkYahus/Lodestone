package team.lodestar.lodestone.registry.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

public class LodestoneVertexFormats {
    public static VertexFormatElement TANGENT = VertexFormatElement.register(VertexFormatElement.findNextId(),0, VertexFormatElement.Type.BYTE, VertexFormatElement.Usage.NORMAL, 3);
    public static VertexFormatElement BITANGENT = VertexFormatElement.register(VertexFormatElement.findNextId(),0, VertexFormatElement.Type.BYTE, VertexFormatElement.Usage.NORMAL, 3);

    //Scary!!
    public static final VertexFormat POSITION_COLOR_TEX_LIGHTMAP_NORMAL_TANGENT_BITANGENT = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION) // 3 floats 3*4
            .add("Color", VertexFormatElement.COLOR) // 4 bytes 4*1
            .add("UV0", VertexFormatElement.UV0) // 2 floats 2*4
            .add("UV2", VertexFormatElement.UV2) // 2 shorts 2*2
            .add("Normal", VertexFormatElement.NORMAL) // 3 bytes 3*1
            .add("Tangent", TANGENT) // 3 bytes 3*1
            .add("Bitangent", BITANGENT) // 3 bytes 3*1
            .padding(3) //37 total, we add three
            .build();
}
