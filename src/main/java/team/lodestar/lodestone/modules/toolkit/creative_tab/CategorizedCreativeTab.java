package team.lodestar.lodestone.modules.toolkit.creative_tab;

import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

public abstract class CategorizedCreativeTab extends CreativeModeTab {

    final String mod;
    final HashMap<String, CreativeTabCategory> categories = new LinkedHashMap<>();
    final Int2ObjectArrayMap<CreativeTabCategory.CategoryHeader> headers = new Int2ObjectArrayMap<>();

    protected CategorizedCreativeTab(String mod, Builder builder) {
        super(builder);
        this.mod = mod;
        buildCategories();
    }

    public static void buildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() instanceof CategorizedCreativeTab tab) {
            for (CreativeTabCategory category : tab.categories.values()) {
                for (Either<Supplier<ItemStack>, CreativeTabCategory.Operation> either : category.items()) {
                    either.ifLeft(l -> event.accept(l.get()));
                }
            }
        }
    }

    public abstract Optional<ResourceLocation> getHeaderTexture(CreativeTabCategory.CategoryHeader header, int row, int column);

    public abstract Optional<ResourceLocation> getEmptySlotTexture(int row, int column);

    public abstract boolean isItemVisible(ItemStack stack);

    public abstract void buildCategories();

    public HashMap<String, CreativeTabCategory> getCategories() {
        return categories;
    }

    public Int2ObjectArrayMap<CreativeTabCategory.CategoryHeader> getHeaders() {
        return headers;
    }

    public CreativeTabCategoryBuilder createCategory(String id) {
        return new CreativeTabCategoryBuilder(this, mod, id);
    }

    public static Builder builder(Function<Builder, CategorizedCreativeTab> tabFactory) {
        return new Builder(tabFactory, CreativeModeTab.Row.TOP, 0);
    }

    public static class Builder extends CreativeModeTab.Builder {

        public Builder(Function<Builder, CategorizedCreativeTab> tabFactory, Row row, int column) {
            super(row, column);
            withTabFactory(b -> tabFactory.apply((Builder) b));
        }
    }

}