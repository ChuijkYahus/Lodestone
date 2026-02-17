package team.lodestar.lodestone.systems.datagen.providers.sound;

import net.minecraft.data.*;
import net.neoforged.neoforge.common.data.*;
import team.lodestar.lodestone.systems.sound.*;

import java.util.function.*;

public abstract class LodestoneBlockSoundEventProvider extends LodestoneSoundEventProvider {

    public static LodestoneBlockSoundEventProvider INSTANCE;

    public LodestoneBlockSoundEventProvider(PackOutput packOutput, String modId, ExistingFileHelper existingFileHelper) {
        super(packOutput, modId, existingFileHelper);
        INSTANCE = this;
    }

    public void addBlockSoundEvents(RegistryReadyBlockSoundType soundType, String path) {
        addBlockSoundEvents(soundType, path, c -> {
        });
    }

    public void addBlockSoundEvents(RegistryReadyBlockSoundType soundType, String path, Consumer<BlockSoundEventBuilder> modifier) {
        var builder = BlockSoundEventBuilder.create(path, soundType);
        modifier.accept(builder);
        builder.addSounds();
    }

    public SoundEventBuilderBlueprint createBlockSoundEvents(String path) {
        return createBlockSoundEvents(path, c -> {
        });
    }

    public SoundEventBuilderBlueprint createBlockSoundEvents(String path, Consumer<BlockSoundEventBuilder> modifier) {
        return new SoundEventBuilderBlueprint(path, modifier);
    }

    public static class SoundEventBuilderBlueprint {
        protected final String path;
        protected final Consumer<BlockSoundEventBuilder> modifier;

        public SoundEventBuilderBlueprint(String path, Consumer<BlockSoundEventBuilder> modifier) {
            this.path = path;
            this.modifier = modifier;
        }

        public SoundEventBuilderBlueprint add(RegistryReadyBlockSoundType soundType) {
            INSTANCE.addBlockSoundEvents(soundType, path, modifier);
            return this;
        }

        public SoundEventBuilderBlueprint add(RegistryReadyBlockSoundType soundType, Consumer<BlockSoundEventBuilder> extraModifier) {
            INSTANCE.addBlockSoundEvents(soundType, path, modifier.andThen(extraModifier));
            return this;
        }
    }
}