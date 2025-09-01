#version 400 core

in vec3 Position;
in vec2 UV0;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec2 texCoord;

void main() {
    vec4 vsPos = ModelViewMat * vec4(Position, 1.0);
    gl_Position = ProjMat * vsPos;

    texCoord = UV0;
}
