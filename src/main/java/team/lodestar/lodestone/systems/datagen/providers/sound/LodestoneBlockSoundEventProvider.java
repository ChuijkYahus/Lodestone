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

    public void addBlockSoundType(String path, RegistryReadyBlockSoundType soundType) {
        addBlockSoundType(path, soundType, c -> {
        });
    }

    public void addBlockSoundType(String path, RegistryReadyBlockSoundType soundType, Consumer<BlockSoundEventBuilder> modifier) {
        var builder = BlockSoundEventBuilder.create(path, soundType);
        modifier.accept(builder);
        builder.addSounds();
    }
}