package team.lodestar.lodestone.modules.toolkit.inventory;

import net.minecraft.world.item.ItemStack;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntity;

import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public class LodestoneItemStackBlockHandlerBuilder<T extends LodestoneBlockEntity> extends LodestoneItemStackHandlerBuilder{

    protected final T parent;

    protected ItemStackHandlerItemDisplayData<T> displayData;

    protected LodestoneItemStackBlockHandlerBuilder(T parent, int slotCount) {
        super(slotCount);
        this.parent = parent;
    }

    @Override
    public LodestoneItemStackBlockHandlerBuilder<T> limitItemSize(int allowedItemSize) {
        return (LodestoneItemStackBlockHandlerBuilder<T>) super.limitItemSize(allowedItemSize);
    }

    @Override
    public LodestoneItemStackBlockHandlerBuilder<T> onContentsChanged(Runnable contentsChangeBehavior) {
        return (LodestoneItemStackBlockHandlerBuilder<T>) super.onContentsChanged(contentsChangeBehavior);
    }

    @Override
    public LodestoneItemStackBlockHandlerBuilder<T> setInputPredicate(Predicate<ItemStack> inputPredicate) {
        return (LodestoneItemStackBlockHandlerBuilder<T>) super.setInputPredicate(inputPredicate);
    }

    public LodestoneItemStackBlockHandlerBuilder<T> setDisplayData(ItemStackHandlerItemDisplayData<T> displayData) {
        this.displayData = displayData;
        return this;
    }

    @Override
    public LodestoneItemStackBlockHandler<T> build() {
        return new LodestoneItemStackBlockHandler<>(parent, displayData, slotCount, allowedItemSize, inputPredicate, onContentsChanged);
    }
}
