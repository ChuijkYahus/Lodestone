package team.lodestar.lodestone.modules.toolkit.inventory;

import net.minecraft.world.item.ItemStack;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntity;

import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public class LodestoneItemStackBlockHandlerBuilder extends LodestoneItemStackHandlerBuilder{

    protected final LodestoneBlockEntity parent;

    protected ItemStackHandlerItemDisplayData displayData;

    protected LodestoneItemStackBlockHandlerBuilder(LodestoneBlockEntity parent, int slotCount) {
        super(slotCount);
        this.parent = parent;
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

    public LodestoneItemStackBlockHandlerBuilder setDisplayData(ItemStackHandlerItemDisplayData displayData) {
        this.displayData = displayData;
        return this;
    }

    @Override
    public LodestoneItemStackBlockHandler build() {
        return new LodestoneItemStackBlockHandler(parent, displayData, slotCount, allowedItemSize, inputPredicate, onContentsChanged);
    }
}
