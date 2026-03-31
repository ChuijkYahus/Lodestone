package team.lodestar.lodestone.modules.toolkit.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntity;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntityTicker;

import java.util.function.Predicate;

public class LodestoneItemStackBlockHandler<T extends LodestoneBlockEntity> extends LodestoneItemStackHandler {

    protected final T parent;
    protected final ItemStackHandlerItemDisplayData<T> displayData;

    public LodestoneItemStackBlockHandler(T parent, ItemStackHandlerItemDisplayData<T> displayData, int slotCount, int allowedItemSize, Predicate<ItemStack> inputPredicate, Runnable contentsChangeBehavior) {
        super(slotCount, allowedItemSize, inputPredicate, contentsChangeBehavior);
        this.parent = parent;
        this.displayData = displayData;
        parent.attachTicker(displayData);
    }

    @Override
    public void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        parent.setDirty();
        if (displayData != null) {
            displayData.onContentsChanged(slot);
        }
    }

    public T getParent() {
        return parent;
    }

    public ItemStackHandlerItemDisplayData<T> getDisplayData() {
        return displayData;
    }
}
