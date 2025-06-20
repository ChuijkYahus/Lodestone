package team.lodestar.lodestone.systems.particle.screen;

import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneScreenParticleRenderType;
import team.lodestar.lodestone.systems.particle.world.*;

import java.util.*;
import java.util.function.Consumer;

public class ScreenParticleOptions extends SimpleParticleOptions {

    public final ScreenParticleType<?> type;
    public LodestoneScreenParticleRenderType renderType = LodestoneScreenParticleRenderType.ADDITIVE;

    public final Collection<Consumer<LodestoneScreenParticle>> tickActors = new ArrayList<>();
    public final Collection<Consumer<LodestoneScreenParticle>> spawnActors = new ArrayList<>();
    public final Collection<Consumer<LodestoneScreenParticle>> renderActors = new ArrayList<>();

    public boolean tracksStack;
    public double stackTrackXOffset;
    public double stackTrackYOffset;

    public ScreenParticleOptions(ScreenParticleType<?> type) {
        this.type = type;
    }
}