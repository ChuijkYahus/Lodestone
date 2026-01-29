package team.lodestar.lodestone.systems.creative_tab;

import com.mojang.datafixers.util.*;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.*;
import net.minecraft.util.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;

import java.util.*;

public class CategorizedCreativeTabHandler {

    private static final HashMap<CreativeModeTab, CategorizedCreativeTabWrapper> CATEGORIZED_TABS = new HashMap<>();

    public static void registerCategorizedCreativeTab(CreativeModeTab tab, CategorizedCreativeTabWrapper wrapper) {
        CATEGORIZED_TABS.put(tab, wrapper);
    }
    
    public static Optional<CategorizedCreativeTabWrapper> getOpenCategorizedTab() {
        var selectedTab = CreativeModeInventoryScreen.selectedTab;
        if (!CATEGORIZED_TABS.containsKey(selectedTab)) {
            return Optional.empty();
        }
        return Optional.ofNullable(CATEGORIZED_TABS.get(selectedTab));
    }

    public static void ensureCategoriesAreReal() {
        getOpenCategorizedTab().ifPresent(t -> {
            if (t.getCategories().isEmpty()) {
                t.buildCategories();
            }
        });
    }

    public static void modifyTab(CreativeModeInventoryScreen.ItemPickerMenu menu, CreativeModeTab selectedTab) {
        if (!CATEGORIZED_TABS.containsKey(selectedTab)) {
            return;
        }
        var categorizedTab = CATEGORIZED_TABS.get(selectedTab);
        fillMenu(menu, categorizedTab);
    }

    public static boolean renderSlot(GuiGraphics guiGraphics, Slot slot) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof CreativeModeInventoryScreen screen) {
            if (!(slot instanceof CreativeModeInventoryScreen.CustomCreativeSlot)) {
                return false;
            }
            var optional = getOpenCategorizedTab();
            if (optional.isEmpty()) {
                return false;
            }
            var categorizedTab = optional.get();
            var item = slot.getItem();
            if (item.isEmpty()) {
                var menu = screen.getMenu();
                int row = menu.getRowIndexForScroll(screen.scrollOffs);
                int column = slot.getContainerSlot() % 9;
                int itemIndex = row * 9 + Mth.floor(slot.getSlotIndex() / 9f) * 9;
                var pose = guiGraphics.pose();
                pose.pushPose();
                pose.translate(0.0F, 0.0F, 100.0F);

                var headers = categorizedTab.getHeaders();
                if (headers.containsKey(itemIndex)) {
                    var header = headers.get(itemIndex);
                    var texture = categorizedTab.getHeaderTexture(row, column);
                    if (texture.isPresent()) {
                        var sprite = minecraft.getGuiSprites().getSprite(texture.get());
                        guiGraphics.blit(slot.x - 1, slot.y - 2, 0, 18, 20, sprite);
                    }
                    if (column == 0) {
                        var font = minecraft.font;
                        var title = Component.translatable(header.category().getHeaderLangKey());
                        int x = slot.x + 80 - font.width(title) / 2;
                        int y = slot.y + 1;
                        pose.pushPose();
                        pose.translate(0.0F, 0.0F, 100.0F);
                        guiGraphics.drawString(font, title, x, y, 4210752, false);
                        pose.popPose();
                    }
                } else {
                    var texture = categorizedTab.getEmptySlotTexture(row, column);
                    if (texture.isPresent()) {
                        var sprite = minecraft.getGuiSprites().getSprite(texture.get());
                        guiGraphics.blit(slot.x, slot.y, 0, 16, 16, sprite);
                    }
                }
                pose.popPose();
                return true;
            }
        }
        return false;
    }

    public static boolean disableSlotHighlight(Slot slot) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof CreativeModeInventoryScreen) {
            if (!(slot instanceof CreativeModeInventoryScreen.CustomCreativeSlot)) {
                return false;
            }
            if (getOpenCategorizedTab().isEmpty()) {
                return false;
            }
            return slot.getItem().isEmpty();
        }
        return false;
    }

    public static void fillMenu(CreativeModeInventoryScreen.ItemPickerMenu menu, CategorizedCreativeTabWrapper categorizedTab) {
        var categories = categorizedTab.getCategories().values();
        var items = menu.items;
        categorizedTab.getHeaders().clear();
        items.clear();
        for (CategorizedCreativeTabWrapper.Category category : categories) {
            addCategoryHeader(menu, categorizedTab, category);
            for (Either<ItemStack, CategorizedCreativeTabWrapper.Operation> either : category.items()) {
                either.ifLeft(i -> {
                    if (categorizedTab.isItemVisible(i)) {
                        items.add(i);
                    }
                });
                either.ifRight(e -> clearRow(menu, false));
            }
            clearRow(menu, false);
        }
    }

    public static void addCategoryHeader(CreativeModeInventoryScreen.ItemPickerMenu menu, CategorizedCreativeTabWrapper categorizedTab, CategorizedCreativeTabWrapper.Category category) {
        var items = menu.items;
        var index = items.size();
        clearRow(menu, true);
        categorizedTab.getHeaders().put(index, new CategorizedCreativeTabWrapper.CategoryHeader(category));
    }

    public static void clearRow(CreativeModeInventoryScreen.ItemPickerMenu menu, boolean force) {
        var items = menu.items;
        int missing = 9 - items.size() % 9;
        if (force || missing != 9) {
            for (int i = 0; i < missing; i++) {
                items.add(ItemStack.EMPTY);
            }
        }
    }
}
