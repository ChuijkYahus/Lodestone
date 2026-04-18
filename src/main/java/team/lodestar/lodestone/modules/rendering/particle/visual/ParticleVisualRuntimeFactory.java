package team.lodestar.lodestone.modules.rendering.particle.visual;

@FunctionalInterface
public interface ParticleVisualRuntimeFactory<T> {
    ParticleVisualRuntime create(T config);
}