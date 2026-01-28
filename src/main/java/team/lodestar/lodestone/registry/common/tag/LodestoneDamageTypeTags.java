package team.lodestar.lodestone.registry.common.tag;

import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.damagesource.*;
import team.lodestar.lodestone.*;

public class LodestoneDamageTypeTags {

    public static final TagKey<DamageType> IS_MAGIC = common("is_magic");
    public static final TagKey<DamageType> AFFECTED_BY_MAGIC_RESISTANCE = tag("affected_by_magic_resistance");
    public static final TagKey<DamageType> AFFECTED_BY_MAGIC_PROFICIENCY = tag("affected_by_magic_proficiency");
    public static final TagKey<DamageType> CAN_TRIGGER_MAGIC_DAMAGE = common("can_trigger_magic_damage");
    public static final TagKey<DamageType> IGNORES_MAGIC_ATTACK_COOLDOWN_SCALAR = common("ignores_magic_attack_cooldown_scalar");

    public static TagKey<DamageType> common(String path) {
        return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("c", path));
    }

    public static TagKey<DamageType> tag(String path) {
        return TagKey.create(Registries.DAMAGE_TYPE, path.contains(":") ? ResourceLocation.parse(path) : LodestoneLib.lodestonePath(path));
    }
}