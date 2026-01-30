package team.lodestar.lodestone.systems.creative_tab;

import com.mojang.datafixers.util.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.*;

import java.util.*;
import java.util.function.*;

public class CreativeTabCategoryBuilder {

    protected final CategorizedCreativeTab categorizedTab;
    protected final String mod;
    protected final String id;
    protected final ArrayList<Either<Supplier<ItemStack>, CreativeTabCategory.Operation>> items = new ArrayList<>();

    public CreativeTabCategoryBuilder(CategorizedCreativeTab categorizedTab, String mod, String id) {
        this.categorizedTab = categorizedTab;
        this.mod = mod;
        this.id = id;
    }

    public final CreativeTabCategoryBuilder addItems(Consumer<CreativeTabCategoryBuilder> itemAdder) {
        itemAdder.accept(this);
        return this;
    }

    @SafeVarargs
    public final <T extends Item> CreativeTabCategoryBuilder addItems(DeferredHolder<Item, T>... items) {
        for (DeferredHolder<Item, T> item : items) {
            addItem(item::get);
        }
        return this;
    }

    public CreativeTabCategoryBuilder addItems(Item... items) {
        for (Item item : items) {
            addItem(item);
        }
        return this;
    }

    public CreativeTabCategoryBuilder addItem(Supplier<Item> item) {
        return addItemStack(() -> item.get().getDefaultInstance());
    }

    public CreativeTabCategoryBuilder addItemStack(Supplier<ItemStack> item) {
        items.add(Either.left(item));
        return this;
    }

    public CreativeTabCategoryBuilder addItem(Item item) {
        return addItemStack(item.getDefaultInstance());
    }

    public CreativeTabCategoryBuilder addItemStack(ItemStack item) {
        items.add(Either.left(() -> item));
        return this;
    }

    public CreativeTabCategoryBuilder nextLine() {
        items.add(Either.right(CreativeTabCategory.Operation.NEXT_LINE));
        return this;
    }

    public void bake() {
        categorizedTab.getCategories().put(id, new CreativeTabCategory(mod, id, items));
    }
}