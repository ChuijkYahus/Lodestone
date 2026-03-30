package team.lodestar.lodestone.modules.toolkit.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntity;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntityTicker;

import java.util.function.Predicate;

public class LodestoneItemStackBlockHandler extends LodestoneItemStackHandler implements LodestoneBlockEntityTicker.BlockEntityTickerAttachment {

    protected final LodestoneBlockEntity parent;
    protected ItemStackHandlerItemDisplayData displayData;

    public LodestoneItemStackBlockHandler(LodestoneBlockEntity parent, int slotCount, int allowedItemSize, Predicate<ItemStack> inputPredicate, Runnable contentsChangeBehavior) {
        super(slotCount, allowedItemSize, inputPredicate, contentsChangeBehavior);
        this.parent = parent;
        parent.attachTicker(this);
    }

    @Override
    public void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        parent.setDirty();
        if (displayData != null) {
            displayData.onContentsChanged(slot);
        }
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (displayData != null) {
            displayData.tick();
        }
    }
}
