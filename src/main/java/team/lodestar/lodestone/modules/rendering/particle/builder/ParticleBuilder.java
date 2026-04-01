package team.lodestar.lodestone.modules.rendering.particle.builder;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.modules.rendering.particle.component.ParticleComponentType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ParticleBuilder {
    private final ResourceLocation particleTypeId;
    private final Map<ParticleComponentType<?>, Object> componentConfigs = new LinkedHashMap<>();

    private ParticleBuilder(ResourceLocation particleTypeId) {
        this.particleTypeId = particleTypeId;
    }

    public static ParticleBuilder create(ResourceLocation particleTypeId) {
        return new ParticleBuilder(particleTypeId);
    }

    public <T> ParticleBuilder with(ParticleComponentType<T> type, Consumer<T> configurer) {
        T config = type.configFactory().get();
        configurer.accept(config);
        componentConfigs.put(type, config);
        return this;
    }

    // Evil Java doesnt like overloading generic methods with consumers >:(
    public <T> ParticleBuilder withConfig(ParticleComponentType<T> type, T config) {
        componentConfigs.put(type, config);
        return this;
    }

    public ParticleSpec build() {
        return new ParticleSpec(particleTypeId, componentConfigs);
    }
}