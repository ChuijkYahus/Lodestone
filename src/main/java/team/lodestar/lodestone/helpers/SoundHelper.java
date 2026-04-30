package team.lodestar.lodestone.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.modules.core.easing.Easing;

import java.util.function.*;

/**
 * A helper class containing various methods designed to play sound from a side independent context.
 */
public class SoundHelper {

    //TODO: Turning this into a builder-esque class would be awesome.

    public static void playSoundRandomPitch(Entity target, Supplier<SoundEvent> soundEvent, double minPitch, double maxPitch) {
        playSoundRandomPitch(target, soundEvent.get(), minPitch, maxPitch);
    }

    public static void playSoundRandomPitch(Entity target, Supplier<SoundEvent> soundEvent, double volume, double minPitch, double maxPitch) {
        playSoundRandomPitch(target, soundEvent.get(), volume, minPitch, maxPitch);
    }

    public static void playSoundRandomPitch(Entity target, SoundEvent soundEvent, double minPitch, double maxPitch) {
        playSoundRandomPitch(target, soundEvent, 1, minPitch, maxPitch);
    }

    public static void playSoundRandomPitch(Entity target, SoundEvent soundEvent, double volume, double minPitch, double maxPitch) {
        var random = target.getRandom();
        double pitch = Easing.SINE_IN_OUT.asWeighedRandom(random, minPitch, maxPitch);
        playSound(target, soundEvent, volume, pitch);
    }

    public static void playSound(Entity target, Supplier<SoundEvent> soundEvent) {
        playSound(target, soundEvent, 1f, 1f);
    }

    public static void playSound(Entity target, Supplier<SoundEvent> soundEvent, double volume, double pitch) {
        playSound(target, soundEvent.get(), target.getSoundSource(), volume, pitch);
    }

    public static void playSound(Entity target, SoundEvent soundEvent) {
        playSound(target, soundEvent, 1f, 1f);
    }

    public static void playSound(Entity target, SoundEvent soundEvent, double volume, double pitch) {
        playSound(target, soundEvent, target.getSoundSource(), volume, pitch);
    }

    @SuppressWarnings("resource")
    public static void playSound(Entity target, SoundEvent soundEvent, SoundSource soundSource, double volume, double pitch) {
        target.level().playSound(null, target.getX(), target.getY(), target.getZ(), soundEvent, soundSource, (float) volume, (float) pitch);
    }

    public static void playSoundRandomPitch(Level level, BlockPos pos, SoundEvent soundEvent, SoundSource soundSource, double minPitch, double maxPitch) {
        playSoundRandomPitch(level, pos, soundEvent, soundSource, 1, minPitch, maxPitch);
    }

    public static void playSoundRandomPitch(Level level, BlockPos pos, SoundEvent soundEvent, SoundSource soundSource, double volume, double minPitch, double maxPitch) {
        var random = level.getRandom();
        double pitch = Easing.SINE_IN_OUT.asWeighedRandom(random, minPitch, maxPitch);
        playSound(level, pos, soundEvent, soundSource, volume, pitch);
    }

    public static void playSound(Level level, BlockPos pos, SoundEvent soundEvent, SoundSource soundSource) {
        playSound(level, pos, soundEvent, soundSource, 1, 1);
    }

    public static void playSound(Level level, BlockPos pos, SoundEvent soundEvent, SoundSource soundSource, double volume, double pitch) {
        level.playSound(null, pos, soundEvent, soundSource, (float) volume, (float) pitch);
    }
}