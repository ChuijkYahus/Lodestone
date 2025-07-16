#version 150

uniform sampler2D Sampler0;
uniform sampler3D VolumeTexture;
uniform vec3 CameraPos;

in vec3 localPos; // World position not local

out vec4 fragColor;

float sdBoxFrame( vec3 p, vec3 b, float e ){
    p = abs(p)-b;
    vec3 q = abs(p+e)-e;
    return min(min(
    length(max(vec3(p.x,q.y,q.z),0.0))+min(max(p.x,max(q.y,q.z)),0.0),
    length(max(vec3(q.x,p.y,q.z),0.0))+min(max(q.x,max(p.y,q.z)),0.0)),
    length(max(vec3(q.x,q.y,p.z),0.0))+min(max(q.x,max(q.y,p.z)),0.0));
}

vec3 rayMarchObject(vec3 rayOrigin, vec3 rayDir, float maxDistance, int maxSteps, float epsilon, out bool hit) {
    float distTraveled = 0.0;
    for (int i = 0; i < maxSteps; i++) {
        vec3 rayPos = rayOrigin + rayDir * distTraveled;
        float distFromSDF = texture(VolumeTexture, (rayPos + 1.0) * 0.5).r;
        distFromSDF = min(distFromSDF, sdBoxFrame(rayPos, vec3(1), 0.01));

        if (distFromSDF < epsilon) {
            hit = true;
            return rayPos;
        }

        distTraveled += distFromSDF;
        if (distTraveled > maxDistance) {
            break;
        }
    }
    hit = false;
    return vec3(0.0);
}

vec3 pow3(vec3 v, float p) {
    return vec3(pow(v.x, p), pow(v.y, p), pow(v.z, p));
}

void main() {
    fragColor = vec4(0.0);

    vec3 rayDirWorld = normalize(localPos - CameraPos);
    vec3 rayOriginWorld = localPos;

    float distTraveled;
    int maxStepCount = 100;
    float epsilon = 0.01;


    bool hit;
    vec3 hitPos = rayMarchObject(rayOriginWorld, rayDirWorld, 10.0, maxStepCount, epsilon, hit);
    fragColor = vec4(hitPos, 1.0);

    //fragColor = texture(VolumeTexture, localPos * 0.5 + 0.5);

    if (!hit) {
        discard;
    }
}