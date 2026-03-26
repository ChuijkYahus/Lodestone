package team.lodestar.lodestone.modules.rendering.particle;

import team.lodestar.lodestone.modules.rendering.particle.builder.ParticleSpec;
import team.lodestar.lodestone.modules.rendering.particle.component.ParticleComponentType;
import team.lodestar.lodestone.modules.rendering.particle.component.PostUpdateComponent;
import team.lodestar.lodestone.modules.rendering.particle.component.PreRenderComponent;
import team.lodestar.lodestone.modules.rendering.particle.component.PreUpdateComponent;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleSpawnContext;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleStorageBinding;
import team.lodestar.lodestone.modules.rendering.particle.runtime.ParticleView;
import team.lodestar.lodestone.modules.rendering.particle.storage.ParticleComponentStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class ParticlePool {
    private static final Comparator<ParticleStorageBinding> BINDING_PRIORITY =
            Comparator.comparingInt(a -> a.type().priority());

    private final int capacity;
    private int count;

    private final double[] x, y, z;
    private final double[] vx, vy, vz;
    private final int[] age, lifetime;

    private final ParticleView view;

    private final Map<ParticleComponentType<?>, ParticleStorageBinding> bindings = new HashMap<>();

    private final List<ParticleStorageBinding> preUpdateBindings = new ArrayList<>();
    private final List<ParticleStorageBinding> postUpdateBindings = new ArrayList<>();
    private final List<ParticleStorageBinding> preRenderBindings = new ArrayList<>();

    public ParticlePool(int capacity) {
        this.capacity = capacity;

        this.x = new double[capacity];
        this.y = new double[capacity];
        this.z = new double[capacity];

        this.vx = new double[capacity];
        this.vy = new double[capacity];
        this.vz = new double[capacity];

        this.age = new int[capacity];
        this.lifetime = new int[capacity];

        this.view = new ParticleView(x, y, z, vx, vy, vz, age, lifetime);
    }

    public int capacity() {
        return capacity;
    }

    public int count() {
        return count;
    }

    public boolean isFull() {
        return count >= capacity;
    }

    public ParticleView view() {
        return view;
    }

    public void spawn(ParticleSpec spec, ParticleSpawnContext ctx) {
        if (count >= capacity) {
            return;
        }

        ensureBindings(spec);

        int i = count++;

        x[i] = ctx.x;
        y[i] = ctx.y;
        z[i] = ctx.z;

        vx[i] = ctx.vx;
        vy[i] = ctx.vy;
        vz[i] = ctx.vz;

        age[i] = 0;
        lifetime[i] = ctx.lifetime;

        for (ParticleComponentType<?> type : spec.orderedComponentTypes()) {
            ParticleStorageBinding binding = bindings.get(type);
            Object config = spec.componentConfigs().get(type);
            binding.storage().onSpawn(i, config, ctx, view);
        }
    }

    private void ensureBindings(ParticleSpec spec) {
        for (ParticleComponentType<?> type : spec.orderedComponentTypes()) {
            if (!bindings.containsKey(type)) {
                ParticleComponentStorage storage = type.createStorage(capacity);
                registerBinding(new ParticleStorageBinding(type, storage));
            }
        }
    }

    private void registerBinding(ParticleStorageBinding binding) {
        bindings.put(binding.type(), binding);

        if (binding.type().phases().contains(ParticlePhase.PRE_UPDATE)) {
            preUpdateBindings.add(binding);
            preUpdateBindings.sort(BINDING_PRIORITY);
        }
        if (binding.type().phases().contains(ParticlePhase.POST_UPDATE)) {
            postUpdateBindings.add(binding);
            postUpdateBindings.sort(BINDING_PRIORITY);
        }
        if (binding.type().phases().contains(ParticlePhase.PRE_RENDER)) {
            preRenderBindings.add(binding);
            preRenderBindings.sort(BINDING_PRIORITY);
        }
    }

    public void tick(float dt) {
        for (ParticleStorageBinding binding : preUpdateBindings) {
            if (binding.storage() instanceof PreUpdateComponent c) {
                c.preUpdate(count, dt, view);
            }
        }

        double[] x = this.x, y = this.y, z = this.z;
        double[] vx = this.vx, vy = this.vy, vz = this.vz;
        int[] age = this.age, lifetime = this.lifetime;

        int i = 0;
        while (i < count) {
            int newAge = age[i] + 1;
            age[i] = newAge;

            if (newAge >= lifetime[i]) {
                remove(i);
                continue;
            }

            x[i] += vx[i] * dt;
            y[i] += vy[i] * dt;
            z[i] += vz[i] * dt;
            i++;
        }

        for (ParticleStorageBinding binding : postUpdateBindings) {
            if (binding.storage() instanceof PostUpdateComponent c) {
                c.postUpdate(count, dt, view);
            }
        }
    }

    public void preRender() {
        for (ParticleStorageBinding binding : preRenderBindings) {
            if (binding.storage() instanceof PreRenderComponent c) {
                c.preRender(count, view);
            }
        }
    }

    public void remove(int index) {
        int last = count - 1;

        if (index != last) {
            x[index] = x[last];
            y[index] = y[last];
            z[index] = z[last];

            vx[index] = vx[last];
            vy[index] = vy[last];
            vz[index] = vz[last];

            age[index] = age[last];
            lifetime[index] = lifetime[last];
        }

        for (ParticleStorageBinding binding : bindings.values()) {
            binding.storage().onSwapRemove(index, last, view);
        }

        count--;
    }

    public void clear() {
        while (count > 0) {
            remove(count - 1);
        }
    }
}