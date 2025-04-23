package team.lodestar.lodestone.systems.network.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class NetworkedParticleEffectType {

    public static final Map<String, NetworkedParticleEffectType> EFFECT_TYPES = new LinkedHashMap<>();

    public static final Codec<NetworkedParticleEffectType> CODEC = Codec.STRING.comapFlatMap(s ->
                    DataResult.success(EFFECT_TYPES.get(s)),
            NetworkedParticleEffectType::getId);

    protected final String id;

    public NetworkedParticleEffectType(String id) {
        this.id = id;
        EFFECT_TYPES.put(id, this);
    }

    public String getId() {
        return id;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract Supplier<ParticleEffectActor> get();

    protected ParticleEffectBuilder createEffect() {
        return new ParticleEffectBuilder(this);
    }

    public ParticleEffectBuilder createEffect(BlockPos position) {
        return createEffect().at(position);
    }

    public ParticleEffectBuilder createEffect(Vec3 position) {
        return createEffect().at(position);
    }

    public ParticleEffectBuilder createEffect(Entity target) {
        return createEffect().at(target);
    }

    public static class ParticleEffectBuilder {

        public final NetworkedParticleEffectType type;
        public NetworkedParticleEffectPositionData position;
        public NetworkedParticleEffectColorData color;
        public NetworkedParticleEffectExtraData nbt;

        public ParticleEffectBuilder(NetworkedParticleEffectType type) {
            this.type = type;
        }

        public ParticleEffectBuilder at(BlockPos position) {
            return at(new NetworkedParticleEffectPositionData(position));
        }

        public ParticleEffectBuilder at(Vec3 position) {
            return at(new NetworkedParticleEffectPositionData(position));
        }

        public ParticleEffectBuilder at(Entity target) {
            return at(new NetworkedParticleEffectPositionData(target));
        }

        public ParticleEffectBuilder at(NetworkedParticleEffectPositionData position) {
            this.position = position;
            return this;
        }

        public ParticleEffectBuilder color(Color color) {
            return color(ColorParticleData.create(color).build());
        }

        public ParticleEffectBuilder color(ColorParticleData color) {
            return color(NetworkedParticleEffectColorData.fromColor(color));
        }

        public ParticleEffectBuilder color(NetworkedParticleEffectColorData color) {
            this.color = color;
            return this;
        }

        public ParticleEffectBuilder customData(NetworkedParticleEffectExtraData nbt) {
            this.nbt = nbt;
            return this;
        }

        public ParticleEffectBuilder spawn(ServerLevel level) {
            return spawn(p -> PacketDistributor.sendToPlayersTrackingChunk(level, new ChunkPos(position.getAsBlockPos()), p));
        }

        public ParticleEffectBuilder spawn(Consumer<NetworkedParticleEffectPayload> sender) {
            sender.accept(new NetworkedParticleEffectPayload(type.id, position, color, nbt));
            return this;
        }
    }

    public interface ParticleEffectActor {
        void act(Level level, RandomSource random, NetworkedParticleEffectPositionData positionData, NetworkedParticleEffectColorData colorData, NetworkedParticleEffectExtraData nbtData);
    }
}