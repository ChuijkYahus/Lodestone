package team.lodestar.lodestone.modules.toolkit.inventory;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

import javax.swing.border.AbstractBorder;
import java.util.ArrayList;
import java.util.List;

public final class InventoryInteractionResult {

    public static InventoryInteractionResultBuilder create() {
        return new InventoryInteractionResultBuilder();
    }
    public static final InventoryInteractionResult EMPTY = InventoryInteractionResult.create().build();
    public static class InventoryInteractionResultBuilder {

        private ItemStackTransaction internalChange;
        private ItemStackTransaction externalChange;


        public InventoryInteractionResultBuilder internalChange(ItemStackTransaction internalChange) {
            this.internalChange = internalChange;
            return this;
        }

        public InventoryInteractionResultBuilder externalChange(ItemStackTransaction externalChange) {
            this.externalChange = externalChange;
            return this;
        }

        public InventoryInteractionResult build(ServerLevel level, LodestoneItemStackHandler handler) {
            InventoryInteractionResult result = build();
            handler.processResult(level, result);
            return result;
        }

        protected InventoryInteractionResult build() {
            return new InventoryInteractionResult(internalChange, externalChange);
        }

    }

    private final ItemStackTransaction internalChanges;
    private final ItemStackTransaction externalChanges;

    public InventoryInteractionResult(ItemStackTransaction internalChanges, ItemStackTransaction externalChanges) {
        this.internalChanges = internalChanges;
        this.externalChanges = externalChanges;
    }

    public boolean wasSuccessful() {
        return internalChanges.wasSuccessful() || externalChanges.wasSuccessful();
    }

    public ItemStackTransaction getExternalChanges() {
        return externalChanges;
    }

    public ItemStackTransaction getInternalChanges() {
        return internalChanges;
    }

    public static class ItemStackTransaction {

        public static final ItemStackTransaction EMPTY = new ItemStackTransaction(ItemStack.EMPTY, ItemStack.EMPTY, -1);

        private final ItemStack original;
        private final ItemStack updated;
        private final int slot;

        protected ItemStackTransaction(ItemStack original, ItemStack updated, int slot) {
            this.original = original;
            this.updated = updated;
            this.slot = slot;
        }

        public static ItemStackTransaction unchanged(ItemStack stack, int slot) {
            return new ItemStackTransaction(stack, stack, slot);
        }

        public static ItemStackTransaction updated(ItemStack original, ItemStack updated, int slot) {
            return new ItemStackTransaction(original, updated, slot);
        }

        public ItemStack getOriginal() {
            return original;
        }

        public ItemStack getUpdated() {
            return updated;
        }

        public int getSlot() {
            return slot;
        }

        public int getExchangedCount(int clamp) {
            int leftover = original.getCount() - updated.getCount();
            return Math.min(leftover, clamp);
        }

        public boolean wasSuccessful() {
            return !original.equals(updated);
        }
    }
}