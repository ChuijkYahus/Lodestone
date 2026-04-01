package team.lodestar.lodestone.modules.rendering.handlers;

import net.minecraft.core.BlockPos;
import net.neoforged.fml.common.EventBusSubscriber;
import team.lodestar.lodestone.modules.rendering.particle.builder.ParticleSpec;
import team.lodestar.lodestone.modules.rendering.particle.pool.ParticlePool;
import team.lodestar.lodestone.modules.rendering.particle.pool.ParticlePoolGroup;
import team.lodestar.lodestone.modules.rendering.particle.pool.ParticlePoolKey;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleSpawnContext;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//@EventBusSubscriber
public class ParticleHandler {
    private static final int DEFAULT_POOL_CAPACITY = 10000;

    private static final Map<ParticlePoolKey, ParticlePoolGroup> poolGroups = new HashMap<>();

    public static void spawn(ParticleSpec spec, ParticleSpawnContext ctx) {
        ParticlePoolGroup group = getPoolGroup(spec);
        spawn(spec, ctx, group);
    }

    public static void spawn(ParticleSpec spec, ParticleSpawnContext ctx, int count) {
        ParticlePoolGroup group = getPoolGroup(spec);
        for (int i = 0; i < count; i++) {
            spawn(spec, ctx, group);
        }
    }

    public static void spawnWithinBlock(ParticleSpec spec, ParticleSpawnContext ctx, BlockPos pos) {

    }

    public static void spawnWithinBlock(ParticleSpec spec, ParticleSpawnContext ctx, BlockPos pos, int count) {

    }

    public static void spawn(ParticleSpec spec, ParticleSpawnContext ctx, ParticlePoolGroup group) {
        group.claimPool().spawn(spec, ctx);
    }

    public static ParticlePoolGroup getPoolGroup(ParticleSpec spec) {
        ParticlePoolKey key = ParticlePoolKey.fromSpec(spec);
        return poolGroups.computeIfAbsent(key, k -> new ParticlePoolGroup(k, DEFAULT_POOL_CAPACITY));
    }

    public static void tickAll(float dt) {
        for (ParticlePoolGroup group : poolGroups.values()) {
            group.tick(dt);
        }
    }

    public static void preRenderAll() {
        for (ParticlePoolGroup group : poolGroups.values()) {
            group.preRender();
        }
    }

    public static Collection<ParticlePoolGroup> allPoolGroups() {
        return Collections.unmodifiableCollection(poolGroups.values());
    }
}