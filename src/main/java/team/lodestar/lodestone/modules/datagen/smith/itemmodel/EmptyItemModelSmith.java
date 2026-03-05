package team.lodestar.lodestone.modules.datagen.smith.itemmodel;

import net.minecraft.world.item.Item;
import team.lodestar.lodestone.modules.datagen.providers.item.LodestoneItemModelSystem;

import java.util.function.Supplier;

public class EmptyItemModelSmith extends ItemModelSmith{
    public EmptyItemModelSmith() {
        super(null);
    }

    @Override
    public ItemModelSmithResult act(ItemModelSmithProcessor data, Supplier<? extends Item> registryObject) {
        return null;
    }

    @Override
    public ItemModelSmithResult act(LodestoneItemModelSystem provider, Supplier<? extends Item> registryObject) {
        return null;
    }
}
