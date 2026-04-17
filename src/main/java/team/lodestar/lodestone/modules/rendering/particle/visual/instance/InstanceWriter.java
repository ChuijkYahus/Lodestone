package team.lodestar.lodestone.modules.rendering.particle.visual.instance;

import net.minecraft.client.Camera;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleView;
import java.nio.FloatBuffer;

public interface InstanceWriter {
    int write(ParticleView particles, int liveCount, int targetId, float partialTick, Camera camera, FloatBuffer buffer, int startInstance, int elementOffset, int strideFloats);
}