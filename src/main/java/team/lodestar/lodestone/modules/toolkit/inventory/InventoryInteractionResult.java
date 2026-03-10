package team.lodestar.lodestone.modules.toolkit.inventory;

import net.minecraft.world.item.ItemStack;

public record InventoryInteractionResult(ResultType resultType, ItemStack original, ItemStack result) {

    public enum ResultType {
        INSERT,
        EXTRACT,
        FAILURE
    }

    public static InventoryInteractionResult unchanged(ResultType resultType, ItemStack unchanged) {
        return new InventoryInteractionResult(resultType, unchanged, unchanged);
    }

    public static InventoryInteractionResult success(ResultType resultType, ItemStack original, ItemStack result) {
        return new InventoryInteractionResult(resultType, original, result);
    }

    public static InventoryInteractionResult failure() {
        return new InventoryInteractionResult(ResultType.FAILURE, ItemStack.EMPTY, ItemStack.EMPTY);
    }

    public boolean wasSuccessful() {
        return !original.equals(result);
    }

    public int getLeftoverCount(int clamp) {
        int leftover = original.getCount() - result.getCount();
        return Math.min(leftover, clamp);
    }
}
