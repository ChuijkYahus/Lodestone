package team.lodestar.lodestone.systems.enchanting;

import com.mojang.datafixers.util.Either;
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

    public static <T extends EnchantmentEntityEffect> LocatedEnchantmentEffect<T> findEnchantmentEffect(ItemStack item, LodestoneEntityEnchantmentEffectFilter<T> filter) {
        AtomicReference<LocatedEnchantmentEffect<T>> result = new AtomicReference<>(new LocatedEnchantmentEffect.EmptyEnchantmentEffect<>());
        try {
            LodestoneEnchantmentDataHelper.runIterationOnItem(item, filter.getEnchantmentFilter(), (enchantment, enchantmentLevel) -> {
                var componentMap = enchantment.value().effects();
                var matchingEffect = findEnchantmentEffect(item, componentMap, filter);
                Optional<T> resultingEffect = matchingEffect.map(o -> o, filter::breakDeadlock);
                resultingEffect.ifPresent(t -> result.set(new LocatedEnchantmentEffect<>(t, enchantment, enchantmentLevel)));
            }, () -> result.get() != null);
        } catch (Exception ignored) {
            return result.get();
        }
        return result.get();
    }

    public static <T extends EnchantmentEntityEffect> Either<Optional<T>, List<T>> findEnchantmentEffect(ItemStack item, DataComponentMap map, LodestoneEntityEnchantmentEffectFilter<T> filter) {
        List<T> effects = getEntityEffects(item, map, filter);
        if (effects.isEmpty()) {
            return Either.left(Optional.empty());
        }
        if (effects.size() > 1) {
            var effect = effects.getFirst();
            if (effects.stream().allMatch(e -> e.equals(effect))) {
                return Either.left(Optional.of(effect));
            }
            return Either.right(effects);
        }
        return Either.left(Optional.of(effects.getFirst()));
    }

    public static <T extends EnchantmentEntityEffect> List<T> getEntityEffects(ItemStack item, DataComponentMap map, LodestoneEntityEnchantmentEffectFilter<T> filter) {
        return LodestoneEnchantmentDataHelper.getMatchingEffects(item, filter.getComparisonBroker(item), map, filter.getClassFilter(), filter.asCondition());
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