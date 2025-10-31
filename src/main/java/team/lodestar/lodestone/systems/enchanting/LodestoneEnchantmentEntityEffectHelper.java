package team.lodestar.lodestone.systems.enchanting;

import net.minecraft.core.component.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.*;
import team.lodestar.lodestone.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

public class LodestoneEnchantmentEntityEffectHelper {

    public static <T extends EnchantmentEntityEffect> boolean hasEnchantmentEffect(ItemStack item, LodestoneEntityEnchantmentEffectFilter<T> filter) {
        return findEnchantmentEffect(item, filter).isPresent();
    }

    public static <T extends EnchantmentEntityEffect> Optional<LocatedEnchantmentEffect<T>> findEnchantmentEffect(ItemStack item, LodestoneEntityEnchantmentEffectFilter<T> filter) {
        AtomicReference<LocatedEnchantmentEffect<T>> result = new AtomicReference<>(null);
        try {
            LodestoneEnchantmentDataHelper.runIterationOnItem(item, filter.getEnchantmentFilter(), (enchantment, enchantmentLevel) -> {
                var componentMap = enchantment.value().effects();
                var matchingEffect = findEnchantmentEffect(item, componentMap, filter);
                if (matchingEffect.isEmpty()) {
                    result.set(new LocatedEnchantmentEffect.EmptyEnchantmentEffect<>());
                }
                else {
                    result.set(new LocatedEnchantmentEffect<>(matchingEffect.get(), enchantmentLevel));
                }

            }, () -> result.get() != null);
        } catch (Exception ignored) {
            return Optional.empty();
        }
        return Optional.ofNullable(result.get());
    }

    public static <T extends EnchantmentEntityEffect> Optional<T> findEnchantmentEffect(ItemStack item, DataComponentMap map, LodestoneEntityEnchantmentEffectFilter<T> filter) {
        List<T> effects = getEntityEffects(item, map, filter);
        if (effects.isEmpty()) {
            return Optional.empty();
        }
        if (effects.size() > 1) {
            //TODO: This should cycle through the many effects found and display just one at a given time.
            // Example use case: Wind Up has a stronger duration when used on a tool. Axes are both a tool and a melee weapon and as such wind-up is multi purpose there
            // We arrive at a situation where we have 2 separate effects and so we will sort through them
            LodestoneLib.LOGGER.warn("Cannot isolate an enchantment effect instance.");
            return Optional.empty();
        }
        return Optional.of(effects.getFirst());
    }

    public static <T extends EnchantmentEntityEffect> List<T> getEntityEffects(ItemStack item, DataComponentMap map, LodestoneEntityEnchantmentEffectFilter<T> filter) {
        return LodestoneEnchantmentDataHelper.getMatchingEffects(item, map, filter.getClassFilter(), filter.asCondition());
    }

    /**
     * Finds all {@link LevelBasedValue} instances stored on an {@link EnchantmentEntityEffect} through the use of reflection
     *
     * @param effect The effect to sieve through
     * @return All {@link LevelBasedValue} instances stored on an {@link EnchantmentEntityEffect} in order of definition
     */
    public static List<LevelBasedValue> findEntityEffectValues(EnchantmentEntityEffect effect) {
        var values = new ArrayList<LevelBasedValue>();
        var clazz = effect.getClass();

        record Accessor(Class<?> type, Supplier<Object> getter) {
        }

        List<Accessor> accessors = new ArrayList<>();

        if (clazz.isRecord()) {
            for (RecordComponent component : clazz.getRecordComponents()) {
                var accessor = component.getAccessor();
                accessors.add(
                        new Accessor(component.getType(), () -> {
                            try {
                                return accessor.invoke(effect);
                            } catch (Exception e) {
                                throw new RuntimeException("Failed to access record: " + component.getName(), e);
                            }
                        })
                );
            }
        } else {
            // Enchantment Components generally don't use classes but let's just be safe
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                accessors.add(
                        new Accessor(field.getType(), () -> {
                            try {
                                return field.get(effect);
                            } catch (Exception e) {
                                throw new RuntimeException("Failed to access field: " + field.getName(), e);
                            }
                        })
                );
            }
        }

        for (Accessor accessor : accessors) {
            Object object = accessor.getter.get();
            if (object instanceof LevelBasedValue value) {
                values.add(value);
            } else if (object instanceof Optional<?> optional) {
                if (optional.isPresent() && optional.get() instanceof LevelBasedValue value) {
                    values.add(value);
                }
            }
        }
        return values;
    }

}