package team.lodestar.lodestone.registry.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.model.IRenderableModel;
import team.lodestar.lodestone.systems.model.geo.BedrockGeometryModel;
import team.lodestar.lodestone.systems.model.obj.ObjModel;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = LodestoneLib.LODESTONE, value = Dist.CLIENT)
public class LodestoneModels {
    public static List<IRenderableModel> MODELS = new ArrayList<>();

    public static final ObjModel SUZANNE = register(ObjModel.Builder.of(LodestoneLib.lodestonePath("models/suzanne.obj"))
            .build()
    );
    
    public static <T extends IRenderableModel> T register(T model) {
        MODELS.add(model);
        return model;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void loadModels(FMLClientSetupEvent event) {
        MODELS.forEach(IRenderableModel::loadModel);
    }

    public static void cleanup() {
        MODELS.forEach(IRenderableModel::cleanup);
    }
}
