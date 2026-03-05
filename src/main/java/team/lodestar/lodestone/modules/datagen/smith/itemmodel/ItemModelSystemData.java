package team.lodestar.lodestone.modules.datagen.smith.itemmodel;

import net.minecraft.world.item.Item;
import team.lodestar.lodestone.modules.datagen.providers.item.LodestoneItemModelSystem;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record ItemModelSystemData(LodestoneItemModelSystem provider, Consumer<Supplier<? extends Item>> consumer) {
}
