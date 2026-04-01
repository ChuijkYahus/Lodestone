package team.lodestar.lodestone.modules.rendering.particle.builder;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.modules.rendering.particle.pool.ParticlePool;
import team.lodestar.lodestone.modules.rendering.particle.component.ParticleComponentType;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleSpawnContext;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleSpawnContextChain;
import team.lodestar.lodestone.modules.rendering.particle.runtime.profile.ParticleSpawnProfile;

import java.util.*;

public  class ParticleSpec {
    private static final Comparator<ParticleComponentType<?>> COMPONENT_PRIORITY =
            Comparator.comparingInt(ParticleComponentType::priority);

    private final ResourceLocation particleTypeId;
    private final Map<ParticleComponentType<?>, Object> componentConfigs;
    private final ParticleComponentType<?>[] orderedComponentTypes;

    public ParticleSpec(ResourceLocation particleTypeId, Map<ParticleComponentType<?>, Object> componentConfigs) {
        this.particleTypeId = Objects.requireNonNull(particleTypeId, "particleTypeId");
        this.componentConfigs = Collections.unmodifiableMap(new LinkedHashMap<>(componentConfigs));

        List<ParticleComponentType<?>> ordered = new ArrayList<>(this.componentConfigs.keySet());
        ordered.sort(COMPONENT_PRIORITY);
        this.orderedComponentTypes = ordered.toArray(new ParticleComponentType<?>[0]);
    }

    public ResourceLocation particleTypeId() {
        return particleTypeId;
    }

    public Map<ParticleComponentType<?>, Object> componentConfigs() {
        return componentConfigs;
    }

    public ParticleComponentType<?>[] orderedComponentTypes() {
        return orderedComponentTypes;
    }

    public void spawn(ParticlePool pool, ParticleSpawnContext ctx, int count) {
        for (int i = 0; i < count; i++) {
            this.spawn(pool, ctx);
        }
    }

    public void spawn(ParticlePool pool, ParticleSpawnContext ctx) {
        pool.spawn(this, ctx);
    }

    public void spawn(ParticlePool pool, ParticleSpawnProfile profile, int count) {
        this.spawn(pool, new ParticleSpawnContext(), profile, count);
    }

    public void spawn(ParticlePool pool, ParticleSpawnContext ctx, ParticleSpawnProfile profile, int count) {
        for (int i = 0; i < count; i++) {
            ParticleSpawnContext copy = ctx.copy();
            profile.apply(copy);
            pool.spawn(this, copy);
        }
    }

    public void spawn(ParticlePool pool, ParticleSpawnContextChain ctxChain, int count) {
        this.spawn(pool, new ParticleSpawnContext(), ctxChain, count);
    }

    public void spawn(ParticlePool pool, ParticleSpawnContext ctx, ParticleSpawnContextChain ctxChain, int count) {
        for (int i = 0; i < count; i++) {
            pool.spawn(this, ctxChain.apply(ctx.copy(), i, count));
        }
    }
}