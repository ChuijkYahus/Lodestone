package team.lodestar.lodestone.modules.datagen.smith.itemmodel.data;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.lodestar.lodestone.modules.datagen.providers.item.LodestoneItemModelSystem;
import team.lodestar.lodestone.modules.datagen.smith.itemmodel.ItemModelSmith;
import team.lodestar.lodestone.modules.datagen.smith.itemmodel.ItemModelSmithResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ItemModelSystemData {
    private final LodestoneItemModelSystem provider;
    private final Set<Item> items;

    public ItemModelSystemData(LodestoneItemModelSystem provider, DeferredRegister.Items items) {
        this(provider, items.getEntries());
    }

    public ItemModelSystemData(LodestoneItemModelSystem provider, Collection<? extends Supplier<? extends Item>> items) {
        this.provider = provider;
        this.items = items.stream().map(Supplier::get).collect(Collectors.toSet());
    }

    public LodestoneItemModelSystem provider() {
        return provider;
    }

    public Set<Item> items() {
        return items;
    }

    public DatagenItemQuery ofClass(Class<? extends Item> clazz) {
        return new DatagenItemQuery(items.stream().filter(clazz::isInstance));
    }

    public DatagenItemQuery fromList(ItemLike... items) {
        return new DatagenItemQuery(Arrays.stream(items).map(ItemLike::asItem));
    }

    @SafeVarargs
    public final DatagenItemQuery fromList(Supplier<? extends Item>... items) {
        return new DatagenItemQuery(Arrays.stream(items).map(Supplier::get));
    }

    public ItemModelSmithResult approveAct(ItemModelSmith smith, Item item) {
        var result = smith.act(provider, item);
        items.remove(item);
        return result;
    }
}