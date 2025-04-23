package team.lodestar.lodestone.systems.network.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;

import javax.annotation.Nullable;

public class NetworkedParticleEffectPayload extends OneSidedPayloadData {

    private final String id;
    private final NetworkedParticleEffectPositionData positionData;
    @Nullable
    private final NetworkedParticleEffectColorData colorData;
    @Nullable
    private final NetworkedParticleEffectExtraData nbtData;

    public NetworkedParticleEffectPayload(String id, NetworkedParticleEffectPositionData positionData, @Nullable NetworkedParticleEffectColorData colorData, @Nullable NetworkedParticleEffectExtraData nbtData) {
        this.id = id;
        this.positionData = positionData;
        this.colorData = colorData;
        this.nbtData = nbtData;
    }

    public NetworkedParticleEffectPayload(FriendlyByteBuf buf) {
        this.id = buf.readUtf();
        this.positionData = NetworkedParticleEffectPositionData.STREAM_CODEC.decode(buf);
        this.colorData = buf.readBoolean() ? NetworkedParticleEffectColorData.STREAM_CODEC.decode(buf) : null;
        this.nbtData = buf.readBoolean() ? new NetworkedParticleEffectExtraData(buf.readNbt()) : null;
    }

    @Override
    public void serialize(FriendlyByteBuf buf) {
        buf.writeUtf(id);
        NetworkedParticleEffectPositionData.STREAM_CODEC.encode(buf, positionData);

        boolean nonNullColorData = colorData != null;
        buf.writeBoolean(nonNullColorData);
        if (nonNullColorData) {
            NetworkedParticleEffectColorData.STREAM_CODEC.encode(buf, colorData);
        }

        boolean nonNullCompoundTag = nbtData != null;
        buf.writeBoolean(nonNullCompoundTag);
        if (nonNullCompoundTag) {
            buf.writeNbt(nbtData.compoundTag);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(IPayloadContext iPayloadContext) {
        Minecraft instance = Minecraft.getInstance();
        ClientLevel level = instance.level;
        NetworkedParticleEffectType particleEffectType = NetworkedParticleEffectType.EFFECT_TYPES.get(id);
        if (particleEffectType == null) {
            throw new RuntimeException("This shouldn't be happening.");
        }
        NetworkedParticleEffectType.ParticleEffectActor particleEffectActor = particleEffectType.get().get();
        particleEffectActor.act(level, level.random, positionData, colorData, nbtData);
    }
}