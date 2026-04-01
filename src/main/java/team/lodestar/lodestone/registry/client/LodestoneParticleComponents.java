package team.lodestar.lodestone.registry.client;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticlePhase;
import team.lodestar.lodestone.modules.rendering.particle.component.ParticleComponentType;
import team.lodestar.lodestone.modules.rendering.particle.component.types.color.ColorConfig;
import team.lodestar.lodestone.modules.rendering.particle.component.types.color.ColorStorage;

import java.util.*;

public class LodestoneParticleComponents {
    private static final Map<ResourceLocation, ParticleComponentType<?>> REGISTRY = new LinkedHashMap<>();
    private static final Map<ParticleComponentType<?>, Integer> REGISTRY_IDS = new HashMap<>();
    private static int nextId = 0;

    public static final ParticleComponentType<ColorConfig> COLOR =
            LodestoneParticleComponents.register(
                    ParticleComponentType.<ColorConfig>builder(LodestoneLib.lodestonePath("color"))
                            .configFactory(ColorConfig::new)
                            .storageFactory(ColorStorage::new)
                            .phases(ParticlePhase.PRE_RENDER)
                            .priority(0)
                            .build()
            );

    public static <T> ParticleComponentType<T> register(ParticleComponentType<T> type) {
        Objects.requireNonNull(type, "type");
        if (REGISTRY.containsKey(type.id())) {
            throw new IllegalArgumentException("Duplicate particle component id: " + type.id());
        }
        REGISTRY.put(type.id(), type);
        REGISTRY_IDS.put(type, nextId++);
        return type;
    }

    public static ParticleComponentType<?> get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    public static Collection<ParticleComponentType<?>> all() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }

    public static int getRegistryId(ParticleComponentType<?> type) {
        return REGISTRY_IDS.get(type);
    }
}
