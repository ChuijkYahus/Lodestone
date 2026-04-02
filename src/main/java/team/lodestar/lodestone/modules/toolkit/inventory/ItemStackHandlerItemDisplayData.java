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
@SuppressWarnings("unused")
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
            float angle = getAngleForItem(visibleItem, tickedStacks, actualStacks);
            float itemTurn = getItemRotationRateForItem(visibleItem, tickedStacks, actualStacks);
            float distance = getDistanceForItem(visibleItem, tickedStacks, actualStacks);
            float lift = getLiftForItem(visibleItem, tickedStacks, actualStacks);
            float scale = getItemScaleForItem(visibleItem, tickedStacks, actualStacks);
            visibleItem.tick(this, angle, itemTurn, distance, lift, scale);
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
        var blockEntity = handler.parent;
        var random = Objects.requireNonNull(blockEntity.getLevel()).random;
        var seed = random.nextLong();
        var entry = new ItemDisplayDataEntry(stack, seed);
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


    public Optional<Vec3> getItemPosition(int index) {
        if (dataEntries[index] != null) {
            return Optional.of(getItemPosition(dataEntries[index]));
        }
        return Optional.empty();
    }

    public Vec3 getItemPosition(ItemDisplayDataEntry dataEntry) {
        return getItemPosition(dataEntry, 0);
    }

    public Vec3 getItemPosition(ItemDisplayDataEntry dataEntry, float partialTicks) {
        var center = getDisplayCenter(partialTicks);
        float distance = dataEntry.getDistance(partialTicks);
        float angle = dataEntry.getAngle(partialTicks) + getTurn(partialTicks);
        var x = center.x + Math.sin(angle) * distance;
        var y = center.y + dataEntry.getLift(partialTicks);
        var z = center.z + Math.cos(angle) * distance;
        return new Vec3(x, y, z);
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
        return 0.157f;
    }

    public float getTurn(float partialTicks) {
        return Mth.lerp(partialTicks, oldTurn, turn);
    }

    public static class ItemDisplayDataEntry {
        protected final ItemStack stack;
        protected final long seed;
        protected float scale, oldScale;
        protected float angle, oldAngle;
        protected float distance, oldDistance;
        protected float lift, oldLift;
        protected float itemAngle, oldItemAngle;
        protected int age;

        public ItemDisplayDataEntry(ItemStack stack, long seed) {
            this.stack = stack;
            this.seed = seed;
        }

        public ItemDisplayDataEntry setAngle(float angle) {
            this.angle = angle;
            this.oldAngle = angle;
            return this;
        }

        public ItemDisplayDataEntry setDistance(float distance) {
            this.distance = distance;
            this.oldDistance = distance;
            return this;
        }

        public ItemDisplayDataEntry setLift(float lift) {
            this.lift = lift;
            this.oldLift = lift;
            return this;
        }

        public ItemDisplayDataEntry setItemAngle(float itemAngle) {
            this.itemAngle = itemAngle;
            this.oldItemAngle = itemAngle;
            return this;
        }

        public void tick(ItemStackHandlerItemDisplayData data,
                         float targetAngle, float addedItemAngle, float targetDistance, float targetLift, float targetScale) {
            oldAngle = angle;
            angle = DataHelper.approach(angle, targetAngle, data.turnCorrectionRate);

            oldItemAngle = itemAngle;
            itemAngle += addedItemAngle;

            oldDistance = distance;
            distance = targetDistance;

            oldLift = lift;
            lift = targetLift;

            oldScale = scale;
            scale = targetScale;
            age++;
        }

        public long getSeed() {
            return seed;
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

        public ItemStack getStack() {
            return stack;
        }

        public int getAge() {
            return age;
        }

    }
}