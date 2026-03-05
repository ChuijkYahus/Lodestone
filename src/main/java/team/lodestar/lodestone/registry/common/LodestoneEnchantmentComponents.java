package team.lodestar.lodestone.registry.common;

import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.neoforged.neoforge.registries.*;
import team.lodestar.lodestone.*;
import team.lodestar.lodestone.modules.toolkit.enchanting.*;

import java.util.*;
import java.util.function.Supplier;

public class LodestoneEnchantmentComponents {
    public static final DeferredRegister<DataComponentType<?>> ENCHANTMENT_COMPONENTS = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, LodestoneLib.LODESTONE);

    public static final Supplier<DataComponentType<List<LodestoneSlotBasedEnchantmentAttributeEffect>>> SLOT_BOUND_ATTRIBUTES =
            ENCHANTMENT_COMPONENTS.register("slot_bound_attributes", () ->
                    DataComponentType.<List<LodestoneSlotBasedEnchantmentAttributeEffect>>builder()
                            .persistent(LodestoneSlotBasedEnchantmentAttributeEffect.CODEC.codec().listOf())
                            .build()
            );

    public static final LootContextParamSet ENCHANTED_ENTITY = LootContextParamSets.register(
            "lodestone_enchanted_entity",
            builder -> builder.required(LootContextParams.THIS_ENTITY)
                    .required(LootContextParams.ENCHANTMENT_LEVEL)
                    .required(LootContextParams.ORIGIN)
                    .optional(LootContextParams.TOOL)
    );
    public static final LootContextParamSet ENCHANTED_DAMAGE = LootContextParamSets.register(
            "lodestone_enchanted_damage",
            builder -> builder.required(LootContextParams.THIS_ENTITY)
                    .required(LootContextParams.ENCHANTMENT_LEVEL)
                    .required(LootContextParams.ORIGIN)
                    .required(LootContextParams.DAMAGE_SOURCE)
                    .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
                    .optional(LootContextParams.ATTACKING_ENTITY)
                    .optional(LootContextParams.TOOL)
    );

    public static Supplier<DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>>> valueEffect(DeferredRegister<DataComponentType<?>> registry,
            String name) {
        return registry.register(name, () ->
                DataComponentType.<List<ConditionalEffect<EnchantmentValueEffect>>>builder()
                        .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ITEM).listOf())
                        .build()
        );
    }

    public static Supplier<DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>>> entityEffect(DeferredRegister<DataComponentType<?>> registry,
            String name) {
        return registry.register(name, () ->
                DataComponentType.<List<ConditionalEffect<EnchantmentEntityEffect>>>builder()
                        .persistent(ConditionalEffect.codec(EnchantmentEntityEffect.CODEC, ENCHANTED_ENTITY).listOf())
                        .build()
        );
    }

    public static Supplier<DataComponentType<List<TargetedConditionalEffect<EnchantmentEntityEffect>>>> targetedEffect(DeferredRegister<DataComponentType<?>> registry,
            String name) {
        return registry.register(name, () ->
                DataComponentType.<List<TargetedConditionalEffect<EnchantmentEntityEffect>>>builder()
                        .persistent(TargetedConditionalEffect.codec(EnchantmentEntityEffect.CODEC, ENCHANTED_DAMAGE).listOf())
                        .build()
        );
    }
}