package team.lodestar.lodestone.modules.toolkit.inventory;

import com.google.common.collect.ImmutableList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ItemStackMultiHandler {

    protected final ImmutableList<LodestoneItemStackHandler> inventories;
    protected final LodestoneItemStackHandler[] asArray;
    protected final Supplier<IItemHandler> exposedInventory;

    protected int recentInteractionIndex;

    public ItemStackMultiHandler(LodestoneItemStackHandler... inventories) {
        this(List.of(inventories));
    }

    public ItemStackMultiHandler(List<LodestoneItemStackHandler> inventories) {
        this.inventories = ImmutableList.<LodestoneItemStackHandler>builder().addAll(inventories).build();
        this.asArray = inventories.toArray(LodestoneItemStackHandler[]::new);
        this.exposedInventory = () -> new CombinedInvWrapper(asArray);
    }

    public ImmutableList<LodestoneItemStackHandler> getInventories() {
        return inventories;
    }

    public Supplier<IItemHandler> getExposedInventory() {
        return exposedInventory;
    }

    public boolean interact(ServerLevel level, Player player, InteractionHand hand) {
        var interactionQueue = new ArrayList<>(inventories);
        if (recentInteractionIndex != -1) {
            var recentHandler = asArray[recentInteractionIndex];
            interactionQueue.remove(recentHandler);
            var result = interact(level, recentHandler, player, hand);
            if (result.map(InventoryInteractionResult::wasSuccessful).orElse(false)) {
                return true;
            } else {
                recentInteractionIndex = -1;
            }
        }
        for (LodestoneItemStackHandler handler : interactionQueue) {
            var result = interact(level, handler, player, hand);
            if (result.map(InventoryInteractionResult::wasSuccessful).orElse(false)) {
                recentInteractionIndex = inventories.indexOf(handler);
                return true;
            }
        }
        return false;
    }

    public Optional<InventoryInteractionResult> interact(ServerLevel level, LodestoneItemStackHandler handler, Player player, InteractionHand hand) {
        return handler.interact(level, player, hand);
    }
}