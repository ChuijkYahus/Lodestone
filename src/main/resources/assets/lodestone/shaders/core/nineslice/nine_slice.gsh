#version 400 core

layout (triangles) in;
layout(triangle_strip, max_vertices = 3) out;

in DATA {
    vec3 localVertexPos;
    vec2 uv;
} data_in[];

out vec2 texCoord;
flat out vec2 size;

void main() {
    size = vec2(
        length(data_in[0].localVertexPos - data_in[1].localVertexPos),
        length(data_in[1].localVertexPos - data_in[2].localVertexPos)
    );

    for (int i = 0; i < gl_in.length(); i++) {
        gl_Position = gl_in[i].gl_Position;
        texCoord = data_in[i].uv;
        EmitVertex();
    }
    EndPrimitive();
}