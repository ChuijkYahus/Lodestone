package team.lodestar.lodestone.modules.rendering.particle.pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParticlePoolGroup {
    private final ParticlePoolKey key;
    private final List<ParticlePool> pools = new ArrayList<>();
    private final int poolCapacity;

    public ParticlePoolGroup(ParticlePoolKey key, int poolCapacity) {
        this.key = key;
        this.poolCapacity = poolCapacity;
    }

    public ParticlePoolKey key() {
        return key;
    }

    public List<ParticlePool> pools() {
        return Collections.unmodifiableList(pools);
    }

    public ParticlePool claimPool() {
        for (ParticlePool pool : pools) {
            if (!pool.isFull()) {
                return pool;
            }
        }
        ParticlePool pool = new ParticlePool(poolCapacity);
        pools.add(pool);
        return pool;
    }

    public void tick(float dt) {
        for (ParticlePool pool : pools) {
            if (pool.count() > 0) {
                pool.tick(dt);
            }
        }
    }

    public boolean isEmpty() {
        for (ParticlePool pool : pools) {
            if (pool.count() > 0) {
                return false;
            }
        }
        return true;
    }
}