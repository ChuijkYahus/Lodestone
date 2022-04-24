package com.sammy.ortus.systems.rendering.particle.type;

import com.mojang.serialization.Codec;
import com.sammy.ortus.systems.rendering.particle.world.GenericParticle;
import com.sammy.ortus.systems.rendering.particle.world.WorldParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;

import javax.annotation.Nullable;

public class OrtusParticleType extends ParticleType<WorldParticleOptions> {
    public OrtusParticleType() {
        super(false, WorldParticleOptions.DESERIALIZER);
    }


    @Override
    public Codec<WorldParticleOptions> codec() {
        return WorldParticleOptions.codecFor(this);
    }

    public static class Factory implements ParticleProvider<WorldParticleOptions> {
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(WorldParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new GenericParticle(world, data, (ParticleEngine.MutableSpriteSet) sprite, x, y, z, mx, my, mz);
        }
    }
}