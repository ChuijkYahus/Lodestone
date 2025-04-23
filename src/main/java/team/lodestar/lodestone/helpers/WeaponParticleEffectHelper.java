package team.lodestar.lodestone.helpers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.network.WeaponParticleEffectType;
import team.lodestar.lodestone.systems.network.particle.NetworkedParticleEffectColorData;
import team.lodestar.lodestone.systems.network.particle.NetworkedParticleEffectExtraData;
import team.lodestar.lodestone.systems.network.particle.NetworkedParticleEffectType;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;

/**
 * A Helper class designed for creating networked weapon particle effects, such as sword slashes or any kinda jab.
 */
public class WeaponParticleEffectHelper {

    public static WeaponParticleEffectBuilder createEffect(WeaponParticleEffectType effectType) {
        return createEffect(effectType, (d, b) -> WeaponParticleEffectType.createData(d, b.isMirrored, b.slashAngle));
    }
    public static WeaponParticleEffectBuilder createEffect(NetworkedParticleEffectType effectType, WeaponParticleEffectBuilder.EffectDataSupplier supplier) {
        return new WeaponParticleEffectBuilder(effectType, supplier);
    }

    public static class WeaponParticleEffectBuilder {

        protected final NetworkedParticleEffectType effectType;
        protected NetworkedParticleEffectColorData color;
        protected final EffectDataSupplier supplier;
        protected float horizontalOffset;
        protected float slashAngle;
        protected boolean isMirrored;

        protected Vec3 positionOffset = Vec3.ZERO;
        protected float horizontalDeviationStrength = 0, verticalDeviationStrength = 0, forwardOffset = 0, deviationAngle = 0;

        protected WeaponParticleEffectBuilder(NetworkedParticleEffectType effectType, EffectDataSupplier supplier) {
            this.effectType = effectType;
            this.supplier = supplier;
        }

        /**
         * Rotates the effect to appear vertical and offsets it slightly to the right, intended for swing effects that originate from the right hand.
         */
        public WeaponParticleEffectBuilder setVertical() {
            return setVerticalSlashAngle().setHorizontalOffset(0.4f);
        }

        /**
         * Offsets the effect left or right relative to the effect's direction by a given distance.
         *
         * @param horizontalOffset the distance to offset the effect by.
         */
        public WeaponParticleEffectBuilder setHorizontalOffset(float horizontalOffset) {
            this.horizontalOffset = horizontalOffset;
            return this;
        }

        /**
         * Rotates the effect to appear vertical intended for swing effects that originate from up to down.
         */
        public WeaponParticleEffectBuilder setVerticalSlashAngle() {
            return setSlashAngle(1.57f);
        }

        /**
         * Randomizes the angle of the effect.
         */
        public WeaponParticleEffectBuilder setRandomSlashAngle(RandomSource randomSource) {
            return setSlashAngle(randomSource.nextFloat() * 3.14f);
        }

        /**
         * Sets the slash angle of the effect.
         *
         * @param slashAngle The angle that will be applied.
         */
        public WeaponParticleEffectBuilder setSlashAngle(float slashAngle) {
            this.slashAngle = slashAngle;
            return this;
        }

        /**
         * Randomly mirrors the effect.
         */
        public WeaponParticleEffectBuilder mirrorRandomly(RandomSource randomSource) {
            return setMirrored(randomSource.nextBoolean());
        }

        /**
         * Mirrors the effect.
         *
         * @param isMirrored Whether the effect will be mirrored.
         */
        public WeaponParticleEffectBuilder setMirrored(boolean isMirrored) {
            this.isMirrored = isMirrored;
            return this;
        }

        /**
         * Sets the color of the effect.
         *
         * @param color The color that will be applied.
         */
        public WeaponParticleEffectBuilder setColor(NetworkedParticleEffectColorData color) {
            this.color = color;
            return this;
        }

        /**
         * Adds a position offset to the effect. Not relative to anything
         *
         * @param offset The offset to apply.
         */
        public WeaponParticleEffectBuilder setPositionOffset(float offset) {
            return setPositionOffset(offset, offset, offset);
        }

        /**
         * Adds a position offset to the effect. Not relative to anything
         *
         * @param x The x offset to apply.
         * @param y The y offset to apply.
         * @param z The z offset to apply.
         */
        public WeaponParticleEffectBuilder setPositionOffset(float x, float y, float z) {
            return setPositionOffset(new Vec3(x, y, z));
        }

        /**
         * Adds a position offset to the effect. Not relative to anything
         *
         * @param offset The offset to apply.
         */
        public WeaponParticleEffectBuilder setPositionOffset(Vec3 offset) {
            this.positionOffset = offset;
            return this;
        }

        /**
         * Adds horizontal deviation to the effect's direction.
         * @param offset The offset to apply horizontally.
         */
        public WeaponParticleEffectBuilder setHorizontalDirectionDeviation(float offset) {
            this.horizontalDeviationStrength = offset;
            return this;
        }

        /**
         * Adds vertical deviation to the effect's direction.
         * @param offset The offset to apply vertically.
         */
        public WeaponParticleEffectBuilder setVerticalDirectionDeviation(float offset) {
            this.verticalDeviationStrength = offset;
            return this;
        }

        /**
         * Adds deviation to the effect direction.
         * @param offset The offset to apply.
         * @param angle The angle in which to apply the deviation to the effect.
         */
        public WeaponParticleEffectBuilder setDirectionDeviation(float offset, float angle) {
            return setDirectionDeviation(offset, offset, angle);
        }

        /**
         * Adds deviation to the effect direction.
         * @param xDeviation The offset to apply horizontally.
         * @param yDeviation The offset to apply vertically.
         * @param angle The angle in which to apply the deviation to the effect.
         */
        public WeaponParticleEffectBuilder setDirectionDeviation(float xDeviation, float yDeviation, float angle) {
            this.horizontalDeviationStrength = xDeviation;
            this.verticalDeviationStrength = yDeviation;
            this.deviationAngle = angle;
            return this;
        }

        /**
         * Adds a forwards offset to the effect in the effect's direction.
         * @param forwardOffset The amount to move forward by.
         */
        public WeaponParticleEffectBuilder setForwardOffset(float forwardOffset) {
            this.forwardOffset = forwardOffset;
            return this;
        }

        protected Vec3 getPosition(Vec3 position, Vec3 slashDirection) {
            return position.add(positionOffset).add(slashDirection.scale(forwardOffset));
        }

        protected Vec3 getDirection(Vec3 direction) {
            if (horizontalDeviationStrength == 0 && verticalDeviationStrength == 0 && deviationAngle == 0) {
                return direction;
            }
            float yRot = ((float) (Mth.atan2(direction.x, direction.z) * (double) (180F / (float) Math.PI)));
            float yaw = (float) Math.toRadians(yRot);
            var left = new Vec3(-Math.cos(yaw), 0, Math.sin(yaw));
            var up = left.cross(direction);
            float leftOffset = horizontalDeviationStrength;
            float upOffset = verticalDeviationStrength;
            if (horizontalDeviationStrength != 0 && verticalDeviationStrength != 0) {
                leftOffset *= Mth.sin(deviationAngle);
                upOffset *= Mth.cos(deviationAngle);
            }
            return direction
                    .add(left.scale(leftOffset))
                    .add(up.scale(upOffset))
                    .normalize();
        }

        /**
         * Spawns the effect at the attacker's position, using the attacker's look angle as direction.
         * @param attacker The entity to tie the effect to.
         */
        public void spawnForwardSlashingParticle(Entity attacker) {
            spawnForwardSlashingParticle(attacker, attacker.getLookAngle());
        }

        /**
         * Spawns the effect at the attacker's position with a given direction
         * @param attacker The entity to tie the effect to.
         * @param slashDirection The direction to use for the effect.
         */
        public void spawnForwardSlashingParticle(Entity attacker, Vec3 slashDirection) {
            float yRot = ((float) (Mth.atan2(slashDirection.x, slashDirection.z) * (double) (180F / (float) Math.PI)));
            float yaw = (float) Math.toRadians(yRot);
            var left = new Vec3(-Math.cos(yaw), 0, Math.sin(yaw));
            var up = left.cross(slashDirection);

            var offset = slashDirection.scale(0.4f).add(up.scale(-0.3f));
            if (horizontalOffset != 0) {
                offset = offset.add(left.scale(horizontalOffset));
            }
            spawnForwardSlashingParticle(attacker, offset, slashDirection);
        }

        /**
         * Spawns the effect at the attacker's position with a given offset and direction
         * @param attacker The entity to tie the effect to.
         * @param slashOffset The non-relative offset to use for the effect.
         * @param slashDirection The direction to use for the effect.
         */
        public void spawnForwardSlashingParticle(Entity attacker, Vec3 slashOffset, Vec3 slashDirection) {
            if (attacker.level() instanceof ServerLevel serverLevel) {
                double xOffset = slashOffset.x;
                double yOffset = slashOffset.y + attacker.getBbHeight() * 0.5f;
                double zOffset = slashOffset.z;
                var position = attacker.position().add(xOffset, yOffset, zOffset);
                spawnSlashingParticle(serverLevel, position, slashDirection);
            }
        }

        /**
         * Spawns the effect at the target's position, offset towards the attacker and aimed at the target.
         * @param attacker The entity to tie the effect to.
         * @param target The entity to target the effect with.
         */
        public void spawnTargetBoundSlashingParticle(Entity attacker, Entity target) {
            var direction = attacker.getLookAngle();
            var random = attacker.getRandom();
            float yRot = ((float) (Mth.atan2(direction.x, direction.z) * (double) (180F / (float) Math.PI)));
            float yaw = (float) Math.toRadians(yRot);
            var left = new Vec3(-Math.cos(yaw), 0, Math.sin(yaw));
            var up = left.cross(direction);

            var slashDirection = target.position().subtract(attacker.position()).normalize();
            var offset = direction.scale(-1.4f).add(up.scale(-0.2f)).subtract(slashDirection.scale(0.5f + random.nextFloat() * 0.5f));
            if (horizontalOffset != 0) {
                offset = offset.add(left.scale(horizontalOffset));
            }
            spawnTargetBoundSlashingParticle(attacker, target, offset, slashDirection);
        }

        /**
         * Spawns the effect at the target's position, offset towards the attacker and aimed towards the given direction.
         * @param attacker The entity to tie the effect to.
         * @param target The entity to target the effect with.
         * @param slashDirection The direction to apply to the effect.
         */
        public void spawnTargetBoundSlashingParticle(Entity attacker, Entity target, Vec3 slashDirection) {
            var direction = attacker.getLookAngle();
            var random = attacker.getRandom();
            float yRot = ((float) (Mth.atan2(direction.x, direction.z) * (double) (180F / (float) Math.PI)));
            float yaw = (float) Math.toRadians(yRot);
            var left = new Vec3(-Math.cos(yaw), 0, Math.sin(yaw));
            var up = left.cross(direction);

            var offset = direction.scale(-1.4f).add(up.scale(-0.2f)).subtract(slashDirection.scale(0.5f + random.nextFloat() * 0.5f));
            if (horizontalOffset != 0) {
                offset = offset.add(left.scale(horizontalOffset));
            }
            spawnTargetBoundSlashingParticle(attacker, target, offset, slashDirection);
        }


        /**
         * Spawns the effect at the target's position modified by the given offset and aimed towards the given direction.
         * @param attacker The entity to tie the effect to.
         * @param target The entity to target the effect with.
         * @param slashOffset The offset to apply to the effect.
         * @param slashDirection The direction to apply to the effect.
         */
        public void spawnTargetBoundSlashingParticle(Entity attacker, Entity target, Vec3 slashOffset, Vec3 slashDirection) {
            if (attacker.level() instanceof ServerLevel serverLevel) {
                double xOffset = slashOffset.x;
                double yOffset = slashOffset.y + attacker.getBbHeight() * 0.5f;
                double zOffset = slashOffset.z;
                var position = target.position().add(xOffset, yOffset, zOffset);
                spawnSlashingParticle(serverLevel, position, slashDirection);
            }
        }

        /**
         * Spawns the effect at the given position with the given direction
         * @param slashPosition The position for the effect.
         * @param slashDirection The direction for the effect.
         */
        public void spawnSlashingParticle(ServerLevel level, Vec3 slashPosition, Vec3 slashDirection) {
            if (color == null) {
                color = NetworkedParticleEffectColorData.fromColor(ColorParticleData.createGrayParticleColor(level.getRandom()));
            }
            effectType.createEffect(getPosition(slashPosition, slashDirection))
                    .color(color)
                    .customData(supplier.createData(getDirection(slashDirection), this))
                    .spawn(level);
        }

        /**
         * A supplier for the Extra NBT Data an effect may need. Use to pass in custom data.
         */
        public interface EffectDataSupplier {
            NetworkedParticleEffectExtraData createData(Vec3 direction, WeaponParticleEffectBuilder builder);
        }
    }
}