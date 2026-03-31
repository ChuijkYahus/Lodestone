package team.lodestar.lodestone.modules.toolkit.blockentity;

import com.google.common.collect.ImmutableList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntityTicker.BlockEntityTickerAttachment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class LodestoneBlockEntityType<T extends LodestoneBlockEntity> extends BlockEntityType<T> {

    protected final LodestoneBlockEntityTicker.Type type;

    @SuppressWarnings("DataFlowIssue")
    public LodestoneBlockEntityType(LodestoneBlockEntityTypeBuilder.LodestoneBlockEntitySupplier<? extends T> factory, Set<Block> validBlocks, LodestoneBlockEntityTicker.Type type) {
        super(factory, validBlocks, null);
        this.type = type;
    }

    public final LodestoneBlockEntityTicker<T> getTickerUnsafe(Level level, BlockState state) {
        if (hasTicker(level)) {
            return new LodestoneBlockEntityTicker<>(Collections.emptyList());
        }
        return null;
    }

    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    public final LodestoneBlockEntityTicker<T> tryGetBlockEntityAwareTicker(Level level, BlockState state, LodestoneBlockEntity blockEntity) {
        return getBlockEntityAwareTicker(level, state, (T) blockEntity);
    }

    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    public final LodestoneBlockEntityTicker<T> getBlockEntityAwareTicker(Level level, BlockState state, T blockEntity) {
        if (hasTicker(level)) {
            List<BlockEntityTickerAttachment<T>> result = new ArrayList<>();
            for (Object obj : blockEntity.tickers) {
                result.add((BlockEntityTickerAttachment<T>) obj);
            }
            return new LodestoneBlockEntityTicker<>(ImmutableList.copyOf(result));
        }
        return null;
    }

    public boolean hasTicker(Level level) {
        return switch (type) {
            case BOTH -> true;
            case NONE -> false;
            case SERVER -> level instanceof ServerLevel;
            case CLIENT -> level.isClientSide;
        };
    }
}
