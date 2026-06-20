package team.lodestar.lodestone.modules.toolkit.sound;

import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.registries.*;
import team.lodestar.lodestone.modules.core.easing.*;

import java.util.function.*;

/**
 * A helper class containing various methods designed to play sound from a side independent context.
 */
public class SoundPlayer {

    private float minPitch = 1, maxPitch = 1;
    private float minVolume = 1, maxVolume = 1;
    private final SoundEvent soundEvent;

    public static SoundPlayer create(DeferredHolder<SoundEvent, SoundEvent> soundEvent) {
        return create(soundEvent.value());
    }

    public static SoundPlayer create(Holder<SoundEvent> soundEvent) {
        return create(soundEvent.value());
    }

    public static SoundPlayer create(Supplier<SoundEvent> soundEvent) {
        return create(soundEvent.get());
    }

    public static SoundPlayer create(SoundEvent soundEvent) {
        return new SoundPlayer(soundEvent);
    }

    public SoundPlayer(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
    }

    public SoundPlayer pitch(float pitch) {
        return pitch(pitch, pitch);
    }

    public SoundPlayer pitch(float min, float max) {
        minPitch = min;
        maxPitch = max;
        return this;
    }

    public SoundPlayer volume(float volume) {
        return volume(volume, volume);
    }

    public SoundPlayer volume(float min, float max) {
        minVolume = min;
        maxVolume = max;
        return this;
    }

    public SoundPlayer play(Entity entity) {
        return play(entity.level(), entity.position().add(0, entity.getBbHeight(), 0), entity.getSoundSource());
    }

    public SoundPlayer play(Level level, BlockPos position, SoundSource source) {
        return play(level, position.getCenter(), source);
    }

    public SoundPlayer play(Level level, Position position, SoundSource source) {
        var random = level.random;
        float volume = Easing.SINE_IN_OUT.asWeighedRandom(random, minVolume, maxVolume);
        float pitch = Easing.SINE_IN_OUT.asWeighedRandom(random, minPitch, maxPitch);
        level.playSound(null, position.x(), position.y(), position.z(), soundEvent, source, volume, pitch);
        return this;
    }
}