package team.lodestar.lodestone.systems.model.armor;

import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.extensions.common.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

/**
 * @author SammySemicolon
 * Convenient client item extension responsible for properly linking a specified model with the player model
 */
public class LodestoneArmorClientItemExtensions implements IClientItemExtensions {
	private final Supplier<? extends Model> model;

	public LodestoneArmorClientItemExtensions(Supplier<? extends Model> model) {
		this.model = model;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public @NotNull Model getGenericArmorModel(@NotNull LivingEntity entity, @NotNull ItemStack itemStack, @NotNull EquipmentSlot armorSlot, @NotNull HumanoidModel playerModel) {
		var model = this.model.get();
		if (model instanceof EntityModel entityModel) {
			float delta = Minecraft.getInstance().getTimer().getGameTimeDeltaTicks();
			float f = Mth.rotLerp(delta, entity.yBodyRotO, entity.yBodyRot);
			float f1 = Mth.rotLerp(delta, entity.yHeadRotO, entity.yHeadRot);
			float netHeadYaw = f1 - f;
			float netHeadPitch = Mth.lerp(delta, entity.xRotO, entity.getXRot());
			playerModel.copyPropertiesTo(entityModel);
			if (entityModel instanceof LodestoneArmorModel armorModel) {
				armorModel.slot = armorSlot;
				armorModel.copyFromDefault(playerModel);
			}
			if (!(entityModel instanceof HumanoidModel<?>)) {
				//Humanoid Models have setupAnim called already by now
				entityModel.setupAnim(entity, entity.walkAnimation.position(), entity.walkAnimation.speed(), entity.tickCount + delta, netHeadYaw, netHeadPitch);
			}
		}
		return model;
	}
}