package team.lodestar.lodestone.modules.toolkit.inventory;

import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class LodestoneItemStackHandlerBuilder<T> {

    public final T parent;
    public final int slotCount;
    public int allowedItemSize = 64;
    public Predicate<ItemStack> inputPredicate = s -> true;
    public Runnable onContentsChanged;

    protected LodestoneItemStackHandlerBuilder(T parent, int slotCount) {
        this.parent = parent;
        this.slotCount = slotCount;
    }

    public LodestoneItemStackHandlerBuilder<T> limitItemSize(int allowedItemSize) {
        this.allowedItemSize = allowedItemSize;
        return this;
    }

    public LodestoneItemStackHandlerBuilder<T> setInputPredicate(Predicate<ItemStack> inputPredicate) {
        this.inputPredicate = inputPredicate;
        return this;
    }

    public LodestoneItemStackHandlerBuilder<T> onContentsChanged(Runnable contentsChangeBehavior) {
        this.onContentsChanged = contentsChangeBehavior;
        return this;
    }

    public LodestoneItemStackHandler<T> build() {
        return build(LodestoneItemStackHandler::new);
    }

    public LodestoneItemStackHandler<T> build(Factory<T> factory) {
        return factory.build(parent, slotCount, allowedItemSize, inputPredicate, onContentsChanged);
    }

    public interface Factory<T> {
        LodestoneItemStackHandler<T> build(T parent, int slotCount, int allowedItemSize, Predicate<ItemStack> inputPredicate, Runnable onContentsChanged);
    }
}