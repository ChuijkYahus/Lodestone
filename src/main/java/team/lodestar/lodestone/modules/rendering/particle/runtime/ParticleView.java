package team.lodestar.lodestone.modules.rendering.particle.runtime;

public class ParticleView {
    private final double[] x, y, z;
    private final double[] vx, vy, vz;
    private final int[] age, lifetime;

    public ParticleView(double[] x, double[] y, double[] z, double[] vx, double[] vy, double[] vz, int[] age, int[] lifetime) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.age = age;
        this.lifetime = lifetime;
    }

    public double[] x() { return x; }
    public double[] y() { return y; }
    public double[] z() { return z; }

    public double[] vx() { return vx; }
    public double[] vy() { return vy; }
    public double[] vz() { return vz; }

    public int[] age() { return age; }
    public int[] lifetime() { return lifetime; }
}
