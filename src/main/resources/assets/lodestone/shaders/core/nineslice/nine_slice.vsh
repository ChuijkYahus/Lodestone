#version 400 core

#moj_import <lodestone:common_math.glsl>

in vec3 Position;
in vec2 UV0;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform int FogShape;

out DATA {
    vec3 localVertexPos;
    vec2 uv;
} data_out;

void main() {
    vec4 vsPos = ModelViewMat * vec4(Position, 1.0);
    gl_Position = ProjMat * vsPos;

    data_out.localVertexPos = Position;
    data_out.uv = UV0;
}
