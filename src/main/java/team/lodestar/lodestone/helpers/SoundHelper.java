package team.lodestar.lodestone.helpers;

import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;

/**
 * A helper class containing various methods designed to play sound from a side independent context.
 */
public class SoundHelper {

    public static void playSound(Entity target, SoundEvent soundEvent) {
        playSound(target, soundEvent, 1f, 1f);
    }

    public static void playSound(Entity target, SoundEvent soundEvent, float volume, float pitch) {
        playSound(target, soundEvent, target.getSoundSource(), volume, pitch);
    }

    public static void playSoundRandomPitch(Entity target, SoundEvent soundEvent, float minPitch, float maxPitch) {
        playSound(target, soundEvent, minPitch, maxPitch);
    }

    public static void playSoundRandomPitch(Entity target, SoundEvent soundEvent, float volume, float minPitch, float maxPitch) {
        var random = target.getRandom();
        float pitch = RandomHelper.randomBetween(random, minPitch, maxPitch);
        playSound(target, soundEvent, target.getSoundSource(), volume, pitch);
    }

    @SuppressWarnings("resource")
    public static void playSound(Entity target, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch) {
        target.level().playSound(null, target, soundEvent, soundSource, volume, pitch);
    }
}