package team.lodestar.lodestone.modules.toolkit.blockentity;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntityTicker.Type;

import java.util.Set;

public final class LodestoneBlockEntityTypeBuilder<T extends LodestoneBlockEntity> {
    private final LodestoneBlockEntitySupplier<? extends T> factory;
    final Set<Block> validBlocks;
    private Type type = Type.NONE;

    private LodestoneBlockEntityTypeBuilder(LodestoneBlockEntitySupplier<? extends T> factory, Set<Block> validBlocks) {
        this.factory = factory;
        this.validBlocks = validBlocks;
    }

    public static <T extends LodestoneBlockEntity> LodestoneBlockEntityTypeBuilder<T> of(LodestoneBlockEntitySupplier<? extends T> factory, Block... validBlocks) {
        return new LodestoneBlockEntityTypeBuilder<>(factory, ImmutableSet.copyOf(validBlocks));
    }

    public LodestoneBlockEntityTypeBuilder<T> setTickerType(Type type) {
        this.type = type;
        return this;
    }

    public LodestoneBlockEntityType<T> build() {
        return new LodestoneBlockEntityType<>(factory, validBlocks, type);
    }

    public interface LodestoneBlockEntitySupplier<T extends LodestoneBlockEntity> extends BlockEntityType.BlockEntitySupplier<T> {
    }
}