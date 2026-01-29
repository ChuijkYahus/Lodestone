package team.lodestar.lodestone.systems.creative_tab;

import com.mojang.datafixers.util.*;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;

import java.util.*;

public abstract class CategorizedCreativeTabWrapper {

    final HashMap<String, Category> categories = new LinkedHashMap<>();
    final Int2ObjectArrayMap<CategoryHeader> headers = new Int2ObjectArrayMap<>();

    public abstract Optional<ResourceLocation> getHeaderTexture(int row, int column);

    public abstract Optional<ResourceLocation> getEmptySlotTexture(int row, int column);

    public abstract boolean isItemVisible(ItemStack stack);

    public abstract void buildCategories();

    public HashMap<String, Category> getCategories() {
        return categories;
    }

    public Int2ObjectArrayMap<CategoryHeader> getHeaders() {
        return headers;
    }

    public CreativeTabCategoryBuilder createCategory(String mod, String id) {
        return new CreativeTabCategoryBuilder(this, mod, id);
    }

    public record Category(String mod, String id, List<Either<ItemStack, Operation>> items) {

        public String getHeaderLangKey() {
            return mod + ".itemGroup.header." + id;
        }
    }

    public record CategoryHeader(Category category) {

    }

    public enum Operation {
        NEXT_LINE
    }
}