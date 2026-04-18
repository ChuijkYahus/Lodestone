package team.lodestar.lodestone.modules.rendering.particle.runtime;

public class ParticleSpawnContext {
    public double x = 0;
    public double y = 0;
    public double z = 0;

    public double vx = 0;
    public double vy = 0;
    public double vz = 0;

    public int lifetime = 20;

    public ParticleSpawnContext position(double[] data) {
        return position(data[0], data[1], data[2]);
    }

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

    public ParticleSpawnContext copy() {
        return new ParticleSpawnContext().position(x, y, z).motion(vx, vy, vz).lifetime(lifetime);
    }
}
