package team.lodestar.lodestone.systems.network.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;

import java.util.Collections;
import java.util.List;

public class NetworkedParticleEffectColorData {

    public static final Codec<NetworkedParticleEffectColorData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ColorParticleData.CODEC.listOf().fieldOf("colors").forGetter(data -> data.colors)
    ).apply(instance, NetworkedParticleEffectColorData::new));

    public static final StreamCodec<ByteBuf, NetworkedParticleEffectColorData> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    private final List<ColorParticleData> colors;
    public int colorCycleCounter;

    public static NetworkedParticleEffectColorData fromColors(List<ColorParticleData> colors) {
        return new NetworkedParticleEffectColorData(colors);
    }

    public static NetworkedParticleEffectColorData fromColor(ColorParticleData color) {
        return fromColors(List.of(color));
    }

    public NetworkedParticleEffectColorData(List<ColorParticleData> colors) {
        this.colors = colors.isEmpty() ? Collections.emptyList() : colors;
    }

    public ColorParticleData getColor() {
        if (colors.size() == 1) {
            return colors.getFirst();
        }
        return colors.get(colorCycleCounter++ % colors.size());
    }
}