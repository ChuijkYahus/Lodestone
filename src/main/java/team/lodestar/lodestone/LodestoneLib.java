package team.lodestar.lodestone;

import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.*;
import net.neoforged.fml.common.*;
import net.neoforged.fml.config.*;
import net.neoforged.neoforge.common.*;
import net.neoforged.neoforge.data.event.*;
import org.apache.logging.log4j.*;
import team.lodestar.lodestone.registry.common.LodestoneAttachmentTypes;
import team.lodestar.lodestone.compability.*;
import team.lodestar.lodestone.config.*;
import team.lodestar.lodestone.modules.datagen.*;
import team.lodestar.lodestone.registry.common.*;
import team.lodestar.lodestone.registry.common.particle.*;

@Mod(LodestoneLib.LODESTONE)
public class LodestoneLib {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String LODESTONE = "lodestone";
    public static final RandomSource RANDOM = RandomSource.create();

    public LodestoneLib() {
        IEventBus modBus = ModLoadingContext.get().getActiveContainer().getEventBus();
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);

        LodestoneBlockEntities.BLOCK_ENTITY_TYPES.register(modBus);
        LodestoneParticleTypes.PARTICLES.register(modBus);
        LodestoneRecipeSerializers.RECIPE_SERIALIZERS.register(modBus);
        LodestoneAttachmentTypes.ATTACHMENT_TYPES.register(modBus);
        LodestonePlacementFillers.MODIFIERS.register(modBus);
        LodestoneEnchantmentComponents.ENCHANTMENT_COMPONENTS.register(modBus);
        LodestoneWorldEventTypes.WORLD_EVENT_TYPES.register(modBus);
        LodestoneCommandArgumentTypes.register(modBus);

        CuriosCompat.init();
    }

    public static ResourceLocation lodestonePath(String path) {
        return ResourceLocation.fromNamespaceAndPath(LODESTONE, path);
    }
}