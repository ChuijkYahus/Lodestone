package com.sammy.ortus.systems.multiblock;

import com.sammy.ortus.helpers.BlockHelper;
import com.sammy.ortus.setup.OrtusBlockEntityRegistry;
import com.sammy.ortus.systems.blockentity.OrtusBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiBlockComponentEntity extends OrtusBlockEntity {

    public BlockPos corePos;

    public MultiBlockComponentEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public MultiBlockComponentEntity(BlockPos pos, BlockState state) {
        super(OrtusBlockEntityRegistry.MULTIBLOCK_COMPONENT.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        if (corePos != null) {
            BlockHelper.saveBlockPos(tag, corePos, "core_position_");
        }
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        corePos = BlockHelper.loadBlockPos(tag, "core_position_");
        super.load(tag);
    }

    @Override
    public InteractionResult onUse(Player player, InteractionHand hand) {
        if (corePos != null && level.getBlockEntity(corePos) instanceof MultiBlockCoreEntity core) {
            return core.onUse(player, hand);
        }
        return super.onUse(player, hand);
    }

    @Override
    public void onBreak(@Nullable Player player) {
        if (corePos != null && level.getBlockEntity(corePos) instanceof MultiBlockCoreEntity core) {
            core.onBreak(player);
        }
        super.onBreak(player);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (corePos != null && level.getBlockEntity(corePos) instanceof MultiBlockCoreEntity core) {
            return core.getCapability(cap);
        }
        return super.getCapability(cap);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (corePos != null && level.getBlockEntity(corePos) instanceof MultiBlockCoreEntity core) {
            return core.getCapability(cap, side);
        }
        return super.getCapability(cap, side);
    }
}