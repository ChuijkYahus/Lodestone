package team.lodestar.lodestone.systems.datagen.providers;

import net.minecraft.data.*;
import net.minecraft.resources.*;
import net.minecraft.server.packs.*;
import net.minecraft.sounds.*;
import net.neoforged.neoforge.common.data.*;

import java.util.*;
import java.util.function.*;

public abstract class LodestoneSoundEventProvider extends SoundDefinitionsProvider {

    public final String modId;
    public final ExistingFileHelper helper;

    public LodestoneSoundEventProvider(PackOutput packOutput, String modId, ExistingFileHelper existingFileHelper) {
        super(packOutput, modId, existingFileHelper);
        this.modId = modId;
        this.helper = existingFileHelper;
    }

    protected SoundDefinition definition(SoundEvent soundEvent) {
        return SoundDefinition.definition().subtitle(subtitle(soundEvent));
    }

    public void add(Supplier<SoundEvent> soundEvent, Function<SoundEvent, SoundDefinition> definition) {
        add(soundEvent, definition.apply(soundEvent.get()));
    }

    public void addBlockSoundType(Function<LodestoneSoundEventProvider, BlockSoundEventBuilder> supplier) {
        supplier.apply(this).addSounds();
    }

    public SoundDefinition.Sound[] sounds(String name, int variants) {
        SoundDefinition.Sound[] sounds = new SoundDefinition.Sound[variants];
        for (int i = 0; i < variants; i++) {
            var id = name + (i + 1);
            var resourceLocation = name.contains(":") ? ResourceLocation.parse(id) : ResourceLocation.fromNamespaceAndPath(modId, id);
            sounds[i] = sound(resourceLocation);
        }
        return sounds;
    }

    public SoundDefinition.Sound[] sounds(String name, int variants, Consumer<SoundDefinition.Sound> modifier) {
        var sounds = sounds(name, variants);
        for (SoundDefinition.Sound sound : sounds) {
            modifier.accept(sound);
        }
        return sounds;
    }

    public SoundDefinition.Sound[] allSounds(String basePath, String name, Consumer<SoundDefinition.Sound> modifier, String... fallbacks) {
        var sounds = allSounds(basePath, name, fallbacks);
        for (SoundDefinition.Sound sound : sounds) {
            modifier.accept(sound);
        }
        return sounds;
    }

    public SoundDefinition.Sound[] allSounds(String basePath, String name, String... fallbacks) {
        var sounds = new ArrayList<SoundDefinition.Sound>();
        int counter = 1;
        var leftoverFallbacks = new ArrayList<>(List.of(fallbacks));
        while (true) {
            var id = basePath + name + counter;
            var path = name.contains(":") ? ResourceLocation.parse(id) : ResourceLocation.fromNamespaceAndPath(modId, id);
            boolean valid = helper.exists(path, PackType.CLIENT_RESOURCES, ".ogg", "sounds");
            if (valid) {
                sounds.add(sound(path));
            } else {
                if (counter == 1 && !leftoverFallbacks.isEmpty()) {
                    name = basePath + leftoverFallbacks.removeFirst();
                    continue;
                }
                break;
            }
            counter++;
        }
        var array = new SoundDefinition.Sound[sounds.size()];
        for (int i = 0; i < sounds.size(); i++) {
            array[i] = sounds.get(i);
        }
        return array;
    }

    public static SoundDefinition.Sound sound(String modId, String id) {
        return sound(id.contains(":") ? ResourceLocation.parse(id) : ResourceLocation.fromNamespaceAndPath(modId, id));
    }

    public String subtitle(SoundEvent soundEvent) {
        return subtitle(soundEvent.getLocation());
    }

    public String subtitle(ResourceLocation id) {
        return modId + ".subtitle." + id.getPath();
    }

    public static class BlockSoundEventBuilder {

        private final LodestoneSoundEventProvider parent;
        private final String path;

        public final Supplier<SoundEvent> breakSound;
        public final Supplier<SoundEvent> stepSound;
        public final Supplier<SoundEvent> placeSound;
        public final Supplier<SoundEvent> hitSound;
        public final Supplier<SoundEvent> fallSound;

        private Function<SoundDefinition, SoundDefinition> breakSoundModifier = s -> s;
        private Function<SoundDefinition, SoundDefinition> stepSoundModifier = s -> s;
        private Function<SoundDefinition, SoundDefinition> placeSoundModifier = s -> s;
        private Function<SoundDefinition, SoundDefinition> hitSoundModifier = s -> s;
        private Function<SoundDefinition, SoundDefinition> fallSoundModifier = s -> s;

        private String breakSoundName = "break";
        private String stepSoundName = "step";
        private String placeSoundName = "place";
        private String hitSoundName = "hit";
        private String fallSoundName = "fall";

        public BlockSoundEventBuilder(LodestoneSoundEventProvider parent, String path,
                                      Supplier<SoundEvent> breakSound, Supplier<SoundEvent> stepSound, Supplier<SoundEvent> placeSound, Supplier<SoundEvent> hitSound, Supplier<SoundEvent> fallSound) {
            this.parent = parent;
            this.path = path;

            this.breakSound = breakSound;
            this.stepSound = stepSound;
            this.placeSound = placeSound;
            this.hitSound = hitSound;
            this.fallSound = fallSound;
        }

        public BlockSoundEventBuilder modifySounds(Function<SoundDefinition, SoundDefinition> modifier) {
            return this
                    .modifyBreakSound(modifier)
                    .modifyStepSound(modifier)
                    .modifyPlaceSound(modifier)
                    .modifyHitSound(modifier)
                    .modifyFallSound(modifier);
        }

        public BlockSoundEventBuilder modifyBreakSound(Function<SoundDefinition, SoundDefinition> modifier) {
            this.breakSoundModifier = this.breakSoundModifier.andThen(modifier);
            return this;
        }

        public BlockSoundEventBuilder modifyStepSound(Function<SoundDefinition, SoundDefinition> modifier) {
            this.stepSoundModifier = this.stepSoundModifier.andThen(modifier);
            return this;
        }

        public BlockSoundEventBuilder modifyPlaceSound(Function<SoundDefinition, SoundDefinition> modifier) {
            this.placeSoundModifier = this.placeSoundModifier.andThen(modifier);
            return this;
        }

        public BlockSoundEventBuilder modifyHitSound(Function<SoundDefinition, SoundDefinition> modifier) {
            this.hitSoundModifier = this.hitSoundModifier.andThen(modifier);
            return this;
        }

        public BlockSoundEventBuilder modifyFallSound(Function<SoundDefinition, SoundDefinition> modifier) {
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

        public void add(Supplier<SoundEvent> soundEvent, Function<SoundDefinition, SoundDefinition> modifier, String name, String... fallbacks) {
            parent.add(soundEvent, s -> modifier.apply(parent.definition(s).with(parent.allSounds(path, name, fallbacks))));
        }
    }
}

