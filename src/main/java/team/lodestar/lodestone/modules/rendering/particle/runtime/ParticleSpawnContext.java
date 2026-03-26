package team.lodestar.lodestone.modules.rendering.particle.runtime;

public class ParticleSpawnContext {
    public double x;
    public double y;
    public double z;

    public double vx;
    public double vy;
    public double vz;

    public int lifetime = 20;

    public ParticleSpawnContext position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public ParticleSpawnContext motion(double vx, double vy, double vz) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        return this;
    }

    public ParticleSpawnContext lifetime(int lifetime) {
        this.lifetime = lifetime;
        return this;
    }
}
