package team.lodestar.lodestone.modules.rendering.particle.visual.types.trail;

import com.mojang.blaze3d.vertex.VertexBuffer;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.modules.rendering.particle.pool.ParticlePool;
import team.lodestar.lodestone.modules.rendering.particle.visual.*;

public class TrailVisualRuntime implements ParticleVisualRuntime {
    private final TrailVisualConfig config;
    private TrailVisualStorage storage;
    private int initializedCapacity = -1;
    private int initializedMaxPoints = -1;

    public TrailVisualRuntime(TrailVisualConfig config) {
        this.config = config;
    }

    private void ensureStorage(ParticlePool pool) {
        int capacity = pool.capacity();
        int maxPoints = config.maxPoints;

        if (storage == null || initializedCapacity != capacity || initializedMaxPoints != maxPoints) {
            storage = new TrailVisualStorage(capacity, maxPoints);
            initializedCapacity = capacity;
            initializedMaxPoints = maxPoints;
        }
    }

    @Override
    public void collect(ParticleVisualCollectContext context, ParticlePool pool, int liveCount) {
        if (liveCount <= 0 || config.renderType == null) return;

        ensureStorage(pool);
        storage.capture(context.particles(), liveCount, context.targetVisualId());

        var drawData = new TrailVisualDrawData(context.particles(), storage, liveCount, context.targetVisualId(), config.width);
        var submission = new ParticleVisualSubmission(config.renderType, null, null, drawData, TrailBatchRenderer.INSTANCE);
        context.collector().submit(submission);
    }
}