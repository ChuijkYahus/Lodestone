package team.lodestar.lodestone.systems.enchanting;

import net.minecraft.core.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.*;

import java.util.function.*;

public class LocatedEnchantmentEffect<T extends EnchantmentEntityEffect> {
    private final T effect;
    private final Holder<Enchantment> enchantment;
    private final int level;

    public LocatedEnchantmentEffect(T effect, Holder<Enchantment> enchantment, int level) {
        this.effect = effect;
        this.enchantment = enchantment;
        this.level = level;
    }

    public boolean isPresent() {
        return effect != null;
    }

    public T getEffect() {
        return effect;
    }

    public Holder<Enchantment> getEnchantment() {
        return enchantment;
    }

    public int getLevel() {
        return level;
    }

    public float getValue(Function<T, LevelBasedValue> valueGetter, float fallback) {
        LevelBasedValue value = valueGetter.apply(effect);
        return value.calculate(level);
    }

    public static class EmptyEnchantmentEffect<T extends EnchantmentEntityEffect> extends LocatedEnchantmentEffect<T> {

        public EmptyEnchantmentEffect() {
            super(null, null, -1);
        }

        @Override
        public float getValue(Function<T, LevelBasedValue> valueGetter, float fallback) {
            return fallback;
        }
    }
}
