#version 400 core

in vec3 Position;
in vec2 UV0;
in vec2 Size;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

flat out vec2 size;
out vec2 texCoord;

void main() {
    vec4 vsPos = ModelViewMat * vec4(Position, 1.0);
    gl_Position = ProjMat * vsPos;

    texCoord = UV0;
    size = Size;
}
