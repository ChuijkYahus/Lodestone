package team.lodestar.lodestone.modules.toolkit.inventory;

import net.minecraft.world.item.ItemStack;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntity;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class LodestoneItemStackBlockHandlerBuilder extends LodestoneItemStackHandlerBuilder{

    protected final LodestoneBlockEntity parent;

    protected LodestoneItemStackBlockHandlerBuilder(LodestoneBlockEntity parent, int slotCount) {
        super(slotCount);
        this.parent = parent;
    }

    public LodestoneItemStackBlockHandlerBuilder setInputPredicate(BiPredicate<LodestoneBlockEntity, ItemStack> inputPredicate) {
        assert parent != null;
        return (LodestoneItemStackBlockHandlerBuilder) super.setInputPredicate(s -> inputPredicate.test(parent, s));
    }

    @Override
    public LodestoneItemStackBlockHandlerBuilder limitItemSize(int allowedItemSize) {
        return (LodestoneItemStackBlockHandlerBuilder) super.limitItemSize(allowedItemSize);
    }

    @Override
    public LodestoneItemStackBlockHandlerBuilder onContentsChanged(Runnable contentsChangeBehavior) {
        return (LodestoneItemStackBlockHandlerBuilder) super.onContentsChanged(contentsChangeBehavior);
    }

    @Override
    public LodestoneItemStackBlockHandlerBuilder setInputPredicate(Predicate<ItemStack> inputPredicate) {
        return (LodestoneItemStackBlockHandlerBuilder) super.setInputPredicate(inputPredicate);
    }

    @Override
    public LodestoneItemStackBlockHandler build() {
        return new LodestoneItemStackBlockHandler(parent, slotCount, allowedItemSize, inputPredicate, onContentsChanged);
    }
}
