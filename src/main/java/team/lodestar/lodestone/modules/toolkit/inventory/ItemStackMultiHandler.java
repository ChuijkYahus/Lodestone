package team.lodestar.lodestone.modules.toolkit.inventory;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ItemStackMultiHandler {

    protected final ImmutableList<LodestoneItemStackHandler> inventories;
    protected final LodestoneItemStackHandler[] asArray;
    protected final Supplier<IItemHandler> exposedInventory;

    protected int recentInteractionIndex;

    public ItemStackMultiHandler(ImmutableList<LodestoneItemStackHandler> inventories) {
        this.inventories = inventories;
        this.asArray = inventories.toArray(LodestoneItemStackHandler[]::new);
        this.exposedInventory = () -> new CombinedInvWrapper(asArray);
    }

    public boolean interact(Player player, InteractionHand hand) {
        var interactionQueue = new ArrayList<>(inventories);
        if (recentInteractionIndex != -1) {
            var recentHandler = asArray[recentInteractionIndex];
            interactionQueue.remove(recentHandler);
            var result = recentHandler.interact(player, hand);
            if (result.map(InventoryInteractionResult::wasSuccessful).orElse(false)) {
                return true;
            }
        }
        for (LodestoneItemStackHandler handler : interactionQueue) {
            var result = handler.interact(player, hand);
            if (result.map(InventoryInteractionResult::wasSuccessful).orElse(false)) {
                return true;
            }
        }
        return false;
    }
}