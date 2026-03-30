package team.lodestar.lodestone.modules.toolkit.inventory;

import net.minecraft.world.item.ItemStack;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntity;

import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public class LodestoneItemStackBlockHandlerBuilder<T extends LodestoneBlockEntity> extends LodestoneItemStackHandlerBuilder{

    protected final T parent;

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

    @Override
    public LodestoneItemStackBlockHandler<T> build() {
        return new LodestoneItemStackBlockHandler<>(parent, slotCount, allowedItemSize, inputPredicate, onContentsChanged);
    }
}
