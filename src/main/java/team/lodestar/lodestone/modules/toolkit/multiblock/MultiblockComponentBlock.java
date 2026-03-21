package team.lodestar.lodestone.modules.toolkit.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.modules.toolkit.blockentity.IInventoryCapabilityProvider;
import team.lodestar.lodestone.registry.common.*;
import team.lodestar.lodestone.modules.toolkit.block.LodestoneEntityBlock;

/**
 * A basic Multiblock component block.
 */
public class MultiblockComponentBlock extends LodestoneEntityBlock<MultiBlockComponentEntity> implements ILodestoneMultiblockComponent {
    public MultiblockComponentBlock(BlockBehaviour.Properties properties) {
        super(properties);
        setBlockEntity(LodestoneBlockEntities.MULTIBLOCK_COMPONENT);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof MultiBlockComponentEntity provider) {
            var optional = provider.getCore();
            if (optional.isEmpty()) {
                return 0;
            }
            var core = optional.get();
            var capability = level.getCapability(Capabilities.ItemHandler.BLOCK, core.getBlockPos(), core.getBlockState(), core, null);
            if (capability != null) {
                return ItemHandlerHelper.calcRedstoneFromInventory(capability);
            }
        }
        return 0;
    }
}
