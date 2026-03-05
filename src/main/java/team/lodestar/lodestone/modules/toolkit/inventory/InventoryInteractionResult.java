package team.lodestar.lodestone.modules.toolkit.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record InventoryInteractionResult(ItemStack original, ItemStack result) {

    public static InventoryInteractionResult empty() {
        return unchanged(ItemStack.EMPTY);
    }

    public static InventoryInteractionResult unchanged(ItemStack unchanged) {
        return new InventoryInteractionResult(unchanged, unchanged);
    }

    public static InventoryInteractionResult success(ItemStack original, ItemStack result) {
        return new InventoryInteractionResult(original, result);
    }

    public boolean wasSuccessful() {
        return !original.equals(result);
    }

    public int getLeftoverCount(int clamp) {
        int leftover = original.getCount() - result.getCount();
        return Math.min(leftover, clamp);
    }
}
