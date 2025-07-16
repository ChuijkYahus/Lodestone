package team.lodestar.lodestone.registry.client;

import net.minecraft.client.GraphicsStatus;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.model.obj.IndexedModel;
import team.lodestar.lodestone.systems.model.obj.lod.LODStrategy;
import team.lodestar.lodestone.systems.model.obj.lod.MultiLODModel;
import team.lodestar.lodestone.systems.model.obj.ObjModel;
import team.lodestar.lodestone.systems.model.obj.modifier.modifiers.TriangulateModifier;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = LodestoneLib.LODESTONE, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LodestoneOBJModels {
    // TODO: Track models by ResourceLocation & cache their modification history to prevent reparsing and reapplying modifiers
    public static List<ObjModel> OBJ_MODELS = new ArrayList<>();
    public static List<MultiLODModel> LOD_MODELS = new ArrayList<>();

    public static final ObjModel SUZANNE = register(ObjModel.Builder.of(LodestoneLib.lodestonePath("suzanne"))
            .build()
    );

    public static ObjModel register(ObjModel objModel) {
        OBJ_MODELS.add(objModel);
        return objModel;
    }

    public static MultiLODModel register(MultiLODModel lodModel) {
        LOD_MODELS.add(lodModel);
        return lodModel;
    }

    public static void loadModels() {
        OBJ_MODELS.forEach(ObjModel::loadModel);
        LOD_MODELS.forEach(MultiLODModel::loadModel);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onClientSetup(FMLClientSetupEvent event) {
        loadModels();
    }

    public static void cleanup() {
        OBJ_MODELS.forEach(IndexedModel::cleanup);
        LOD_MODELS.forEach(IndexedModel::cleanup);
    }
}
