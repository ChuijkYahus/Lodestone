package team.lodestar.lodestone.events;

import net.minecraft.server.level.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.level.*;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.*;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.network.*;
import team.lodestar.lodestone.systems.easing.*;

@EventBusSubscriber
public class RuntimeEvents {

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        ItemEventHandler.triggerHurtResponses(event);
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Pre event) {
        LodestoneAttributeEventHandler.processAttributes(event);
        ItemEventHandler.triggerHurtResponses(event);
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Post event) {
        ItemEventHandler.triggerHurtResponses(event);
        LodestoneAttributeEventHandler.triggerMagicDamage(event);
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        ItemEventHandler.triggerDeathResponses(event);
    }

    @SubscribeEvent
    public static void entityJoin(EntityJoinLevelEvent event) {
        WorldEventHandler.playerJoin(event);
    }

    @SubscribeEvent
    public static void worldTick(LevelTickEvent.Post event) {
        WorldEventHandler.worldTick(event);
    }
}