package team.lodestar.lodestone.modules.toolkit.blockentity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntityTicker.BlockEntityTickerAttachment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class LodestoneBlockEntityType<T extends LodestoneBlockEntity> extends BlockEntityType<T> {

    protected final LodestoneBlockEntityTicker.TickerType tickerType;

    @SuppressWarnings("DataFlowIssue")
    public LodestoneBlockEntityType(LodestoneBlockEntityBuilder.LodestoneBlockEntitySupplier<? extends T> factory, Set<Block> validBlocks, LodestoneBlockEntityTicker.TickerType tickerType) {
        super(factory, validBlocks, null);
        this.tickerType = tickerType;
    }

    public final LodestoneBlockEntityTicker<T> getTickerUnsafe(Level level, BlockState state) {
        if (hasTicker(level)) {
            return new LodestoneBlockEntityTicker<>(Collections.emptyList());
        }
        return null;
    }

    public final LodestoneBlockEntityTicker<T> getBlockEntityAwareTicker(Level level, BlockState state, LodestoneBlockEntity blockEntity) {
        if (hasTicker(level)) {
            var attachments = blockEntity.tickers;
            return new LodestoneBlockEntityTicker<>(attachments);
        }
        return null;
    }

    public boolean hasTicker(Level level) {
        return switch (tickerType) {
            case BOTH -> true;
            case NONE -> false;
            case SERVER -> level instanceof ServerLevel;
            case CLIENT -> level.isClientSide;
        };
    }
}
