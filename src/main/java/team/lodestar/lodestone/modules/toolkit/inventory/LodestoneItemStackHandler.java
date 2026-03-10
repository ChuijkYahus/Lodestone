package team.lodestar.lodestone.modules.toolkit.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import team.lodestar.lodestone.modules.toolkit.inventory.InventoryInteractionResult.ResultType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * An extension of the ItemStackHandler class, designed to work well when several inventories are relevant in a singular context, such as a block that stores multiple types of items in different lists.
 */
public class LodestoneItemStackHandler extends ItemStackHandler {

    public final int slotCount;
    public final int allowedItemSize;
    public final Predicate<ItemStack> inputPredicate;
    public final Runnable contentsChangeBehavior;

    public ArrayList<ItemStack> nonEmptyItemStacks = new ArrayList<>();

    private int filledSlots;

    public LodestoneItemStackHandler(int slotCount, int allowedItemSize, Predicate<ItemStack> inputPredicate, Runnable contentsChangeBehavior) {
        this.slotCount = slotCount;
        this.allowedItemSize = allowedItemSize;
        this.inputPredicate = inputPredicate;
        this.contentsChangeBehavior = contentsChangeBehavior;
    }

    public NonNullList<ItemStack> getStacks() {
        return stacks;
    }

    public ArrayList<ItemStack> getNonEmptyStacks() {
        return nonEmptyItemStacks;
    }

    public int getFilledSlotCount() {
        return filledSlots;
    }

    public boolean isEmpty() {
        return nonEmptyItemStacks.isEmpty();
    }

    @Override
    public void onContentsChanged(int slot) {
        updateCaches();
        if (contentsChangeBehavior != null) {
            contentsChangeBehavior.run();
        }
    }

    @Override
    public int getSlots() {
        return slotCount;
    }

    @Override
    public int getSlotLimit(int slot) {
        return allowedItemSize;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (!inputPredicate.test(stack)) {
            return false;
        }
        return super.isItemValid(slot, stack);
    }

    public void updateCaches() {
        nonEmptyItemStacks.clear();
        filledSlots = 0;
        for (ItemStack stack : getStacks()) {
            if (!stack.isEmpty()) {
                nonEmptyItemStacks.add(stack);
                filledSlots++;
            }
        }
    }

    public void load(HolderLookup.Provider provider, CompoundTag compound) {
        load(provider, compound, "inventory");
    }

    public void load(HolderLookup.Provider provider, CompoundTag compound, String name) {
        deserializeNBT(provider, compound.getCompound(name));
        if (stacks.size() != slotCount) {
            int missing = slotCount - stacks.size();
            for (int i = 0; i < missing; i++) {
                stacks.add(ItemStack.EMPTY);
            }
        }
        updateCaches();
    }

    public void save(HolderLookup.Provider provider, CompoundTag compound) {
        save(provider, compound, "inventory");
    }

    public void save(HolderLookup.Provider provider, CompoundTag compound, String name) {
        compound.put(name, serializeNBT(provider));
    }

    public void clear() {
        for (int i = 0; i < slotCount; i++) {
            setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void dumpItems(Level level, BlockPos pos) {
        dumpItems(level, pos.getCenter());
    }

    public void dumpItems(Level level, Vec3 pos) {
        for (int i = 0; i < slotCount; i++) {
            if (!getStackInSlot(i).isEmpty()) {
                level.addFreshEntity(new ItemEntity(level, pos.x(), pos.y(), pos.z(), getStackInSlot(i)));
                setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }

    public Optional<InventoryInteractionResult> interact(Player player, InteractionHand hand) {
        updateCaches();
        var heldStack = player.getItemInHand(hand);
        if (heldStack.isEmpty()) {
            var extract = extractItem(player);
            if (extract.wasSuccessful()) {
                return Optional.of(extract);
            }
        }
        else {
            var insert = insertItem(heldStack);
            if (insert.wasSuccessful()) {
                return Optional.of(insert);
            }
        }
        return Optional.empty();
    }

    public InventoryInteractionResult extractItem(Player player) {
        if (isEmpty()) {
            return InventoryInteractionResult.failure();
        }
        var extracted = nonEmptyItemStacks.getLast();
        int slot = stacks.indexOf(extracted);
        var amount = extracted.getCount();
        var simulated = extractItem(slot, amount, true);
        if (simulated.equals(ItemStack.EMPTY)) {
            return InventoryInteractionResult.unchanged(ResultType.EXTRACT, extracted);
        }
        var real = extractItem(slot, amount, false);
        ItemHandlerHelper.giveItemToPlayer(player, real);
        return InventoryInteractionResult.success(ResultType.EXTRACT, real, ItemStack.EMPTY);
    }

    public InventoryInteractionResult insertItem(ItemStack stack) {
        var simulated = insertItem(stack, true);
        if (!simulated.wasSuccessful()) {
            return simulated;
        }
        int count = simulated.getLeftoverCount(allowedItemSize);
        var input = stack.split(count);
        return insertItem(input, false);
    }

    protected InventoryInteractionResult insertItem(ItemStack stack, boolean simulate) {
        ItemStack result = ItemHandlerHelper.insertItem(this, stack, simulate);
        if (result.equals(stack)) {
            return InventoryInteractionResult.unchanged(ResultType.INSERT, result);
        }
        return InventoryInteractionResult.success(ResultType.INSERT, stack, result);
    }
}