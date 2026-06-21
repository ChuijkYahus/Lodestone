package team.lodestar.lodestone.modules.rendering.particle.old_particle_system.screen;

import net.minecraft.client.multiplayer.ClientLevel;
import team.lodestar.lodestone.modules.rendering.particle.old_particle_system.screen.base.ScreenParticle;

public class ScreenParticleType<T extends ScreenParticleOptions> {

    public ParticleProvider<T> provider;

    public ScreenParticleType() {
    }

    public interface ParticleProvider<T extends ScreenParticleOptions> {
        ScreenParticle createParticle(ClientLevel pLevel, T options, double x, double y, double pXSpeed, double pYSpeed);
    }
}