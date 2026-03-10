package team.lodestar.lodestone.modules.toolkit.inventory;

import net.minecraft.world.item.ItemStack;

public record InventoryInteractionResult(ResultType resultType, ItemStack taken, ItemStack received) {

    public enum ResultType {
        INSERT,
        EXTRACT,
        FAILURE
    }

    public static InventoryInteractionResult unchanged(ResultType resultType, ItemStack unchanged) {
        return new InventoryInteractionResult(resultType, unchanged, unchanged);
    }

    public static InventoryInteractionResult success(ResultType resultType, ItemStack taken, ItemStack received) {
        return new InventoryInteractionResult(resultType, taken, received);
    }

    public static InventoryInteractionResult failure() {
        return new InventoryInteractionResult(ResultType.FAILURE, ItemStack.EMPTY, ItemStack.EMPTY);
    }

    public boolean wasSuccessful() {
        return !taken.equals(received);
    }

    public int getLeftoverCount(int clamp) {
        int leftover = taken.getCount() - received.getCount();
        return Math.min(leftover, clamp);
    }
}
