package team.lodestar.lodestone.modules.rendering.particle.type;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.modules.rendering.particle.renderer.ParticleRenderer;

/**
 * Not super happy with this, will change a lot
 */
public class ParticleType {
    private final ResourceLocation id;
    private final ParticleRenderer renderer;

    public ParticleType(ResourceLocation id, ParticleRenderer renderer) {
        this.id = id;
        this.renderer = renderer;
    }
}
