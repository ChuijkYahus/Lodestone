package team.lodestar.lodestone.systems.enchanting;

import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.*;

import javax.annotation.*;
import java.util.function.*;

public class LodestoneEntityEnchantmentEffectFilter<T extends EnchantmentEntityEffect> {


    public static <T extends EnchantmentEntityEffect> LodestoneEntityEnchantmentEffectFilter<T> byType(Class<T> effectClass) {
        return new LodestoneEntityEnchantmentEffectFilter<>(effectClass);
    }

    public static LodestoneEntityEnchantmentEffectFilter<EnchantmentEntityEffect> byRegistry(Holder<MapCodec<? extends EnchantmentEntityEffect>> effectType) {
        return byRegistry(effectType.value());
    }

    public static LodestoneEntityEnchantmentEffectFilter<EnchantmentEntityEffect> byRegistry(MapCodec<? extends EnchantmentEntityEffect> effectType) {
        return new LodestoneEntityEnchantmentEffectFilter<>(EnchantmentEntityEffect.class, effectType);
    }

    private final Class<T> classFilter;
    @Nullable
    private final MapCodec<? extends EnchantmentEntityEffect> typeFilter;
    @Nullable
    private Holder<Enchantment> enchantmentFilter;
    @Nullable
    private ItemStack comparisonBroker;

    public LodestoneEntityEnchantmentEffectFilter(Class<T> classFilter) {
        this(classFilter, null);
    }

    public LodestoneEntityEnchantmentEffectFilter(Class<T> classFilter, @Nullable MapCodec<? extends EnchantmentEntityEffect> typeFilter) {
        this.classFilter = classFilter;
        this.typeFilter = typeFilter;
    }

    public LodestoneEntityEnchantmentEffectFilter<T> withEnchantmentFilter(Holder<Enchantment> enchantment) {
        this.enchantmentFilter = enchantment;
        return this;
    }

    public LodestoneEntityEnchantmentEffectFilter<T> withComparisonBroker(ItemStack comparisonBroker) {
        this.comparisonBroker = comparisonBroker;
        return this;
    }

    protected Either<Predicate<TypedDataComponent<?>>, Predicate<T>> asCondition() {
        return Either.right(e -> (typeFilter == null || e.codec().equals(typeFilter)));
    }

    public Class<T> getClassFilter() {
        return classFilter;
    }

    @Nullable
    public Holder<Enchantment> getEnchantmentFilter() {
        return enchantmentFilter;
    }

    @Nullable
    public ItemStack getComparisonBroker(ItemStack fallback) {
        return comparisonBroker != null ? comparisonBroker : fallback;
    }
}
