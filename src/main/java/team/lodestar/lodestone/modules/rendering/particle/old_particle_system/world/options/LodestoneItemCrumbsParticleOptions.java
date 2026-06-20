package team.lodestar.lodestone.modules.rendering.particle.old_particle_system.world.options;

import net.minecraft.core.particles.*;
import net.minecraft.world.item.*;
import team.lodestar.lodestone.modules.rendering.particle.old_particle_system.world.type.*;

import java.util.function.*;

public class LodestoneItemCrumbsParticleOptions extends WorldParticleOptions {

    public final ItemStack stack;

    public LodestoneItemCrumbsParticleOptions(ParticleType<LodestoneItemCrumbsParticleOptions> type, ItemStack stack) {
        super(type);
        this.stack = stack;
    }

    public LodestoneItemCrumbsParticleOptions(Supplier<? extends LodestoneItemCrumbsParticleType> type, ItemStack stack) {
        this(type.get(), stack);
    }
}