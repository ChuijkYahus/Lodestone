#version 150

in vec3 Position;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

uniform vec3 CameraPos;

out vec3 localPos;

void main() {
    localPos = CameraPos - Position;
    vec4 pos = ModelViewMat * vec4(Position, 1.0);
    gl_Position = ProjMat * pos;
}
