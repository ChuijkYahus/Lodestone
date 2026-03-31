package team.lodestar.lodestone.modules.toolkit.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntity;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntityTicker;

import java.util.ArrayList;

/**
 * A class designed to help with tracking and then displaying items rotating around an object.
 */
public class ItemStackHandlerItemDisplayData implements LodestoneBlockEntityTicker.BlockEntityTickerAttachment {

    protected final LodestoneItemStackBlockHandler handler;

    protected ArrayList<ItemDisplayDataEntry> dataEntries = new ArrayList<>();

    protected final float turnRate, turnCorrectionRate;

    protected final float distanceStepRate, liftStepRate;

    protected float oldTurn, turn;

    public ItemStackHandlerItemDisplayData(LodestoneItemStackBlockHandler handler, float turnRate, float turnCorrectionRate, float distanceStepRate, float liftStepRate) {
        this.handler = handler;
        this.turnRate = turnRate;
        this.turnCorrectionRate = turnCorrectionRate;
        this.distanceStepRate = distanceStepRate;
        this.liftStepRate = liftStepRate;
    }

    public void onContentsChanged(int slot) {

    }

    @Override
    public void tick(LodestoneBlockEntity parent, Level level, BlockPos pos, BlockState state) {
        oldTurn = turn;
        turn += turnRate;
        var stacks = handler.getStacks();

        int tickedStacks = 0;
        int actualStacks = handler.getNonEmptyStacks().size();
        dataEntries.ensureCapacity(stacks.size());
        for (int i = 0; i < stacks.size(); i++) {
            var stack = stacks.get(i);
            if (stack.isEmpty()) {
                dataEntries.set(i, null);
                continue;
            }
            var visibleItem = dataEntries.get(i);
            if (visibleItem == null) {
                visibleItem = addNewItem(i, stack);
            }
            float targetAngle = turn + getAngleForItem(visibleItem, tickedStacks, actualStacks);
            float targetDistance = getDistanceForItem(visibleItem, tickedStacks, actualStacks);
            float targetLift = getLiftForItem(visibleItem, tickedStacks, actualStacks);
            var coordinates = new ItemCoordinates(targetAngle, targetDistance, targetLift);
            visibleItem.tick(this, coordinates);
            tickedStacks++;
        }
    }

    public ItemDisplayDataEntry getEntry(int i) {
        return getDataEntries().get(i);
    }
    public ArrayList<ItemDisplayDataEntry> getDataEntries() {
        return dataEntries;
    }

    public ItemDisplayDataEntry addNewItem(int index, ItemStack stack) {
        var entry = new ItemDisplayDataEntry(stack);
        dataEntries.set(index, entry);
        return entry;
    }

    public final Vec3 getDisplayCenter(float partialTicks) {
        return getDisplayCenter(handler.parent, partialTicks);
    }

    public Vec3 getDisplayCenter(LodestoneBlockEntity parent, float partialTicks) {
        return parent.getBlockPos().getCenter();
    }

    public float getAngleForItem(ItemDisplayDataEntry item, int index, float total) {
        return index / total;
    }

    public float getDistanceForItem(ItemDisplayDataEntry item, int index, float total) {
        return (total - 1) * 0.2f;
    }

    public float getLiftForItem(ItemDisplayDataEntry item, int index, float total) {
        return (1 - Math.min(item.age, 20) / 20f) * 0.25f;
    }

    public static class ItemDisplayDataEntry {
        protected final ItemStack stack;
        protected float angle, oldAngle;
        protected float distance, oldDistance;
        protected float lift, oldLift;
        protected int age;

        public ItemDisplayDataEntry(ItemStack stack) {
            this.stack = stack;
        }

        public void tick(ItemStackHandlerItemDisplayData data, ItemCoordinates coordinates) {
            oldAngle = angle;
            oldDistance = distance;
            angle = DataHelper.approach(angle, coordinates.angle, data.turnRate + data.turnCorrectionRate);
            distance = DataHelper.approach(distance, coordinates.distance, data.distanceStepRate);
            lift = DataHelper.approach(lift, coordinates.lift, data.liftStepRate);
            age++;
        }

        public float getAngle(float partialTicks) {
            return Mth.lerp(oldAngle, angle, partialTicks) % 6.28f;
        }

        public float getDistance(float partialTicks) {
            return Mth.lerp(oldDistance, distance, partialTicks);
        }

        public float getLift(float partialTicks) {
            return Mth.lerp(oldLift, lift, partialTicks);
        }

        public ItemStack getStack() {
            return stack;
        }

        public int getAge() {
            return age;
        }

    }

    public record ItemCoordinates(float angle, float distance, float lift) {

    }
}