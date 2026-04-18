#version 150

in vec3 Position;
in vec2 UV;
in vec4 Color;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

uniform vec3 CamLeft;
uniform vec3 CamUp;

out vec2 outUV;
out vec4 outColor;

void main() {
    //vec3 localPos = (CamLeft * Position.x) + (CamUp * Position.y);
    //vec3 worldPos = localPos;
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    outUV = UV;
    outColor = Color;
}