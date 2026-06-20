package team.lodestar.lodestone.deprecated.particle.world.options;

import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import team.lodestar.lodestone.deprecated.particle.render_types.*;
import team.lodestar.lodestone.deprecated.particle.world.*;
import team.lodestar.lodestone.deprecated.particle.world.behaviors.*;
import team.lodestar.lodestone.deprecated.particle.world.type.*;
import team.lodestar.lodestone.helpers.*;
import team.lodestar.lodestone.modules.rendering.*;
import team.lodestar.lodestone.deprecated.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.rendering.buffer.LodestoneRenderLayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.*;


public class WorldParticleOptions extends SimpleParticleOptions implements ParticleOptions {

    public final ParticleType<?> type;

    public LodestoneParticleBehavior behavior = BillboardParticleBehavior.INSTANCE;
    public ParticleRenderType renderType = LodestoneWorldParticleRenderType.ADDITIVE;
    public LodestoneRenderLayer renderLayer = LodestoneRenderingSystem.DEFERRED_RENDER;

    public final Collection<Consumer<LodestoneWorldParticle>> tickActors = new ArrayList<>();
    public final Collection<Consumer<LodestoneWorldParticle>> spawnActors = new ArrayList<>();
    public final Collection<Consumer<LodestoneWorldParticle>> renderActors = new ArrayList<>();

    public int particleLight = RenderHelper.FULL_BRIGHT;

    public boolean noClip = false;

    public WorldParticleOptions(ParticleType<?> type) {
        this.type = type;
    }

    public WorldParticleOptions(Supplier<? extends LodestoneWorldParticleType> type) {
        this(type.get());
    }

    public WorldParticleOptions setBehavior(LodestoneParticleBehavior behavior) {
        this.behavior = behavior;
        return this;
    }

    public WorldParticleOptions setBehaviorIfDefault(LodestoneParticleBehavior newBehavior) {
        if (behavior.equals(BillboardParticleBehavior.INSTANCE)) {
            return setBehavior(newBehavior);
        }
        return this;
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}