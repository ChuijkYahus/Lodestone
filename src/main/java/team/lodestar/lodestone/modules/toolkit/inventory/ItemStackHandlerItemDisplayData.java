package team.lodestar.lodestone.modules.toolkit.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Math;
import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntity;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntityTicker;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A class designed to help with tracking and then displaying items rotating around an object.
 */
public class ItemStackHandlerItemDisplayData implements LodestoneBlockEntityTicker.BlockEntityTickerAttachment {

    protected final LodestoneItemStackBlockHandler handler;

    protected ItemDisplayDataEntry[] dataEntries = new ItemDisplayDataEntry[]{};

    protected final float turnRate, turnCorrectionRate;

    protected float oldTurn, turn;

    public ItemStackHandlerItemDisplayData(LodestoneItemStackBlockHandler handler,
                                           float turnRate, float turnCorrectionRate) {
        this.handler = handler;
        this.turnRate = turnRate;
        this.turnCorrectionRate = turnCorrectionRate;
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
        if (dataEntries.length != stacks.size()) {
            dataEntries = new ItemDisplayDataEntry[stacks.size()];
        }
        for (int i = 0; i < stacks.size(); i++) {
            var stack = stacks.get(i);
            if (stack.isEmpty()) {
                dataEntries[i] = null;
                continue;
            }
            var visibleItem = dataEntries[i];
            if (visibleItem == null) {
                visibleItem = addNewItem(i, stack);
            }
            float angle = turn + getAngleForItem(visibleItem, tickedStacks, actualStacks);
            float distance = getDistanceForItem(visibleItem, tickedStacks, actualStacks);
            float lift = getLiftForItem(visibleItem, tickedStacks, actualStacks);
            float scale = getItemScaleForItem(visibleItem, tickedStacks, actualStacks);
            float itemRotationRate = getItemRotationRateForItem(visibleItem, tickedStacks, actualStacks);
            visibleItem.tick(this, angle, distance, lift, scale, itemRotationRate);
            tickedStacks++;
        }
    }

    public Optional<ItemDisplayDataEntry> getEntry(int i) {
        return Optional.ofNullable(dataEntries[i]);
    }

    public List<ItemDisplayDataEntry> getDataEntries() {
        return Arrays.stream(dataEntries).filter(Objects::nonNull).toList();
    }

    public ItemDisplayDataEntry addNewItem(int index, ItemStack stack) {
        var entry = new ItemDisplayDataEntry(stack);
        dataEntries[index] = entry;
        return entry;
    }

    public final Vec3 getDisplayCenter() {
        return getDisplayCenter(0);
    }

    public final Vec3 getDisplayCenter(float partialTicks) {
        return getDisplayCenter(handler.parent, partialTicks);
    }

    public Vec3 getDisplayCenter(LodestoneBlockEntity parent, float partialTicks) {
        return parent.getBlockPos().getCenter();
    }

    public float getItemScaleForItem(ItemDisplayDataEntry item, int index, float total) {
        return 1;
    }

    public float getAngleForItem(ItemDisplayDataEntry item, int index, float total) {
        return 6.28f * (index / total);
    }

    public float getDistanceForItem(ItemDisplayDataEntry item, int index, float total) {
        return 1;
    }

    public float getLiftForItem(ItemDisplayDataEntry item, int index, float total) {
        return 0;
    }

    public float getItemRotationRateForItem(ItemDisplayDataEntry item, int index, float total) {
        return 5;
    }

    public static class ItemDisplayDataEntry {
        protected final ItemStack stack;
        protected float scale, oldScale;
        protected float angle, oldAngle;
        protected float distance, oldDistance;
        protected float lift, oldLift;
        protected float itemAngle, oldItemAngle;
        protected int age;

        public ItemDisplayDataEntry(ItemStack stack) {
            this.stack = stack;
        }

        public void tick(ItemStackHandlerItemDisplayData data,
                         float targetAngle, float targetDistance, float targetLift, float targetScale, float itemRotationRate) {
            oldScale = scale;
            oldAngle = angle;
            oldDistance = distance;
            oldLift = lift;
            oldItemAngle = itemAngle;
            angle = DataHelper.approach(angle, targetAngle, data.turnRate + data.turnCorrectionRate);
            distance = targetDistance;
            lift = targetLift;
            scale = targetScale;
            itemAngle += itemRotationRate;
            age++;
        }

        public float getAngle(float partialTicks) {
            return Mth.rotLerp(partialTicks, oldAngle, angle);
        }

        public float getDistance(float partialTicks) {
            return Mth.lerp(partialTicks, oldDistance, distance);
        }

        public float getLift(float partialTicks) {
            return Mth.lerp(partialTicks, oldLift, lift);
        }

        public float getScale(float partialTicks) {
            return Mth.lerp(partialTicks, oldScale, scale);
        }

        public float getItemRotation(float partialTicks) {
            return Mth.rotLerp(partialTicks, oldItemAngle, itemAngle);
        }

        public Vec3 getPosition(Vec3 center) {
            return getPosition(center, 0);
        }

        public Vec3 getPosition(Vec3 center, float partialTicks) {
            float distance = getDistance(partialTicks);
            float angle = getAngle(partialTicks);
            var x = center.x + Math.sin(angle) * distance;
            var y = center.y + getLift(partialTicks);
            var z = center.z + Math.cos(angle) * distance;
            return new Vec3(x, y, z);
        }

        public ItemStack getStack() {
            return stack;
        }

        public int getAge() {
            return age;
        }

    }
}