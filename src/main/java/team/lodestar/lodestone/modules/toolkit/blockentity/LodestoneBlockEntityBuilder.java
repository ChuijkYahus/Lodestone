package team.lodestar.lodestone.modules.toolkit.blockentity;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntityTicker.TickerType;

import java.util.Set;

public final class LodestoneBlockEntityBuilder<T extends LodestoneBlockEntity> {
    private final LodestoneBlockEntitySupplier<? extends T> factory;
    final Set<Block> validBlocks;
    private TickerType tickerType = TickerType.NONE;

    private LodestoneBlockEntityBuilder(LodestoneBlockEntitySupplier<? extends T> factory, Set<Block> validBlocks) {
        this.factory = factory;
        this.validBlocks = validBlocks;
    }

    public static <T extends LodestoneBlockEntity> LodestoneBlockEntityBuilder<T> of(LodestoneBlockEntitySupplier<? extends T> factory, Block... validBlocks) {
        return new LodestoneBlockEntityBuilder<>(factory, ImmutableSet.copyOf(validBlocks));
    }

    public LodestoneBlockEntityBuilder<T> setTickerType(TickerType tickerType) {
        this.tickerType = tickerType;
        return this;
    }

    public LodestoneBlockEntityType<T> build() {
        return new LodestoneBlockEntityType<>(factory, validBlocks, tickerType);
    }

    public interface LodestoneBlockEntitySupplier<T extends LodestoneBlockEntity> extends BlockEntityType.BlockEntitySupplier<T> {
    }
}