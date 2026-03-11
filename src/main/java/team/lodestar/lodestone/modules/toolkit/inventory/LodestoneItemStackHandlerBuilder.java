package team.lodestar.lodestone.modules.toolkit.inventory;

import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class LodestoneItemStackHandlerBuilder {

    public final int slotCount;
    public int allowedItemSize = 64;
    public Predicate<ItemStack> inputPredicate = s -> true;
    public Runnable onContentsChanged;

    protected LodestoneItemStackHandlerBuilder(int slotCount) {
        this.slotCount = slotCount;
    }

    public LodestoneItemStackHandlerBuilder limitItemSize(int allowedItemSize) {
        this.allowedItemSize = allowedItemSize;
        return this;
    }

    public LodestoneItemStackHandlerBuilder setInputPredicate(Predicate<ItemStack> inputPredicate) {
        this.inputPredicate = inputPredicate;
        return this;
    }

    public LodestoneItemStackHandlerBuilder onContentsChanged(Runnable contentsChangeBehavior) {
        this.onContentsChanged = contentsChangeBehavior;
        return this;
    }

    public LodestoneItemStackHandler build() {
        return build(LodestoneItemStackHandler::new);
    }

    public LodestoneItemStackHandler build(Factory factory) {
        return factory.build(slotCount, allowedItemSize, inputPredicate, onContentsChanged);
    }

    public interface Factory {
        LodestoneItemStackHandler build(int slotCount, int allowedItemSize, Predicate<ItemStack> inputPredicate, Runnable onContentsChanged);
    }
}