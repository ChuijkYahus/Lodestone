package team.lodestar.lodestone.systems.rendering.shader;

import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import team.lodestar.lodestone.LodestoneLib;

import java.util.ArrayList;
import java.util.List;

public class LodestoneShaderRegistry {
    public final List<LodestoneShader> shaders = new ArrayList<>();
    public final String modId;

    public LodestoneShaderRegistry(String modId) {
        this.modId = modId;
    }

    public <T extends LodestoneShader> T register(T shader) {
        shaders.add(shader);
        return shader;
    }

    public void init(RegisterShadersEvent event) {
        LodestoneLib.LOGGER.info("Registering shaders for mod: {}", modId);
        shaders.forEach(shader -> shader.register(event));
    }
}
