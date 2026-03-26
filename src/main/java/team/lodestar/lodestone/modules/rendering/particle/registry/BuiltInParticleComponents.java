package team.lodestar.lodestone.modules.rendering.particle.registry;

import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.modules.rendering.particle.ParticlePhase;
import team.lodestar.lodestone.modules.rendering.particle.component.ParticleComponentType;
import team.lodestar.lodestone.modules.rendering.particle.component.types.color.ColorConfig;
import team.lodestar.lodestone.modules.rendering.particle.component.types.color.ColorStorage;
import team.lodestar.lodestone.registry.client.LodestoneParticleComponents;

public final class BuiltInParticleComponents {

    public static final ParticleComponentType<ColorConfig> COLOR =
            LodestoneParticleComponents.register(
                    ParticleComponentType.<ColorConfig>builder(LodestoneLib.lodestonePath("color"))
                            .configFactory(ColorConfig::new)
                            .storageFactory(ColorStorage::new)
                            .phases(ParticlePhase.PRE_RENDER)
                            .priority(0)
                            .build()
            );
}
