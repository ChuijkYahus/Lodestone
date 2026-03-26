package team.lodestar.lodestone.registry.client;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.modules.rendering.particle.ParticlePhase;
import team.lodestar.lodestone.modules.rendering.particle.component.ParticleComponentType;
import team.lodestar.lodestone.modules.rendering.particle.component.types.color.ColorConfig;
import team.lodestar.lodestone.modules.rendering.particle.component.types.color.ColorStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class LodestoneParticleComponents {
    private static final Map<ResourceLocation, ParticleComponentType<?>> REGISTRY = new LinkedHashMap<>();

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
        return type;
    }

    public static ParticleComponentType<?> get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    public static Collection<ParticleComponentType<?>> all() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }
}
