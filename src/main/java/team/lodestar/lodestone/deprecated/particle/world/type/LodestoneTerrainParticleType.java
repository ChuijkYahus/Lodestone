package team.lodestar.lodestone.deprecated.particle.world.type;

import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import team.lodestar.lodestone.deprecated.particle.world.*;
import team.lodestar.lodestone.deprecated.particle.world.options.*;

import javax.annotation.*;

public class LodestoneTerrainParticleType extends AbstractLodestoneParticleType<LodestoneTerrainParticleOptions> {

    public LodestoneTerrainParticleType() {
        super();
    }

    public static class Factory implements ParticleProvider<LodestoneTerrainParticleOptions> {

        public Factory() {
        }

        @Nullable
        @Override
        public Particle createParticle(LodestoneTerrainParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new LodestoneTerrainParticle(world, data, x, y, z, mx, my, mz);
        }
    }
}