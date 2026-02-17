package team.lodestar.lodestone.systems.datagen.providers.sound;

import net.minecraft.sounds.*;
import net.neoforged.neoforge.common.data.*;
import team.lodestar.lodestone.systems.sound.*;

import java.util.function.*;

public class BlockSoundEventBuilder {

    private final String path;

    public final Supplier<SoundEvent> breakSound;
    public final Supplier<SoundEvent> stepSound;
    public final Supplier<SoundEvent> placeSound;
    public final Supplier<SoundEvent> hitSound;
    public final Supplier<SoundEvent> fallSound;

    private Consumer<SoundDefinition.Sound> breakSoundModifier = s -> {
    };
    private Consumer<SoundDefinition.Sound> stepSoundModifier = s -> {
    };
    private Consumer<SoundDefinition.Sound> placeSoundModifier = s -> {
    };
    private Consumer<SoundDefinition.Sound> hitSoundModifier = s -> {
    };
    private Consumer<SoundDefinition.Sound> fallSoundModifier = s -> {
    };

    private String breakSoundName = "break";
    private String stepSoundName = "step";
    private String placeSoundName = "place";
    private String hitSoundName = "hit";
    private String fallSoundName = "fall";

    public static BlockSoundEventBuilder create(String path, RegistryReadyBlockSoundType soundType) {
        return new BlockSoundEventBuilder(path, soundType);
    }

    public BlockSoundEventBuilder(String path, RegistryReadyBlockSoundType soundType) {
        this(path, soundType.getBreakSoundHolder(), soundType.getStepSoundHolder(), soundType.getPlaceSoundHolder(), soundType.getHitSoundHolder(), soundType.getFallSoundHolder());
    }

    public BlockSoundEventBuilder(String path,
                                  Supplier<SoundEvent> breakSound, Supplier<SoundEvent> stepSound, Supplier<SoundEvent> placeSound, Supplier<SoundEvent> hitSound, Supplier<SoundEvent> fallSound) {
        this.path = path;

        this.breakSound = breakSound;
        this.stepSound = stepSound;
        this.placeSound = placeSound;
        this.hitSound = hitSound;
        this.fallSound = fallSound;
    }

    public BlockSoundEventBuilder modifySounds(Consumer<SoundDefinition.Sound> modifier) {
        return this
                .modifyBreakSound(modifier)
                .modifyStepSound(modifier)
                .modifyPlaceSound(modifier)
                .modifyHitSound(modifier)
                .modifyFallSound(modifier);
    }

    public BlockSoundEventBuilder modifyBreakSound(Consumer<SoundDefinition.Sound> modifier) {
        this.breakSoundModifier = this.breakSoundModifier.andThen(modifier);
        return this;
    }

    public BlockSoundEventBuilder modifyStepSound(Consumer<SoundDefinition.Sound> modifier) {
        this.stepSoundModifier = this.stepSoundModifier.andThen(modifier);
        return this;
    }

    public BlockSoundEventBuilder modifyPlaceSound(Consumer<SoundDefinition.Sound> modifier) {
        this.placeSoundModifier = this.placeSoundModifier.andThen(modifier);
        return this;
    }

    public BlockSoundEventBuilder modifyHitSound(Consumer<SoundDefinition.Sound> modifier) {
        this.hitSoundModifier = this.hitSoundModifier.andThen(modifier);
        return this;
    }

    public BlockSoundEventBuilder modifyFallSound(Consumer<SoundDefinition.Sound> modifier) {
        this.fallSoundModifier = this.fallSoundModifier.andThen(modifier);
        return this;
    }

    public BlockSoundEventBuilder breakSoundName(String name) {
        this.breakSoundName = name;
        return this;
    }

    public BlockSoundEventBuilder stepSoundName(String name) {
        this.stepSoundName = name;
        return this;
    }

    public BlockSoundEventBuilder placeSoundName(String name) {
        this.placeSoundName = name;
        return this;
    }

    public BlockSoundEventBuilder hitSoundName(String name) {
        this.hitSoundName = name;
        return this;
    }

    public BlockSoundEventBuilder fallSoundName(String name) {
        this.fallSoundName = name;
        return this;
    }

    public void addSounds() {
        add(breakSound, breakSoundModifier, breakSoundName, "place");
        add(stepSound, stepSoundModifier, stepSoundName, "hit");
        add(placeSound, placeSoundModifier, placeSoundName, "break");
        add(hitSound, hitSoundModifier, hitSoundName, "fall");
        add(fallSound, fallSoundModifier, fallSoundName, "hit", "step");
    }

    public SoundDefinition add(Supplier<SoundEvent> soundEvent, Consumer<SoundDefinition.Sound> modifier, String name, String... fallbacks) {
        return LodestoneBlockSoundEventProvider.INSTANCE.add(soundEvent, s -> s.with(LodestoneBlockSoundEventProvider.INSTANCE.allSounds(path, name, modifier, fallbacks)));
    }
}