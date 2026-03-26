package team.lodestar.lodestone.modules.rendering.particle.builder;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.modules.rendering.particle.ParticlePool;
import team.lodestar.lodestone.modules.rendering.particle.component.ParticleComponentType;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleSpawnContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public  class ParticleSpec {
    private static final Comparator<ParticleComponentType<?>> COMPONENT_PRIORITY =
            Comparator.comparingInt(ParticleComponentType::priority);

    private final ResourceLocation particleTypeId;
    private final Map<ParticleComponentType<?>, Object> componentConfigs;
    private final ParticleComponentType<?>[] orderedComponentTypes;

    ParticleSpec(ResourceLocation particleTypeId, Map<ParticleComponentType<?>, Object> componentConfigs) {
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

    public void spawn(ParticlePool pool, ParticleSpawnContext ctx) {
        pool.spawn(this, ctx);
    }

    public ParticleComponentType<?>[] orderedComponentTypes() {
        return orderedComponentTypes;
    }
}