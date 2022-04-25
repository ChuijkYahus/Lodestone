package com.sammy.ortus.systems.config;

import com.mojang.datafixers.util.Pair;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.*;

/**
 * A config system allowing for static initialization of config values.
 * Value holders are stored in a hashmap, with the key being a pair represented by a configType String and the path of the value
 */
public class OrtusConfig {

    @SuppressWarnings("rawtypes")
    public static final HashMap<Pair<String, ConfigPath>, ArrayList<ConfigValueHolder>> VALUE_HOLDERS = new HashMap<>();

    /**
     * @param configType - an id of sorts for your config to be used as key to your config values. For example: "ortus/common".
     * @param builder    - a forge config builder instance.
     */
    @SuppressWarnings("rawtypes")
    public OrtusConfig(String configType, ForgeConfigSpec.Builder builder) {
        for (Map.Entry<Pair<String, ConfigPath>, ArrayList<ConfigValueHolder>> entry : VALUE_HOLDERS.entrySet()) {
            Pair<String, ConfigPath> s = entry.getKey();
            ArrayList<ConfigValueHolder> h = entry.getValue();
            if (s.getFirst().equals(configType)) {
                builder.push(List.of(s.getSecond().strings));
                h.forEach(v -> v.config = v.valueSupplier.createBuilder(builder));
                builder.pop(s.getSecond().strings.length);
            }
        }

    }

    public static class ConfigValueHolder<T> {
        private final BuilderSupplier<T> valueSupplier;
        private ForgeConfigSpec.ConfigValue<T> config;

        /**
         * @param modId         - Your mod id. Must match whatever you few into the {@link OrtusConfig#OrtusConfig(String, ForgeConfigSpec.Builder)} constructor.
         * @param path          - Path towards your value separated with "/". The first string from a split of your path will be removed and added to the configType.
         * @param valueSupplier - Supplier to your config value. {@link ConfigValueHolder#config} will be set to {@link ConfigValueHolder#valueSupplier#getConfigValue()} when config is initialized.
         */
        public ConfigValueHolder(String modId, String path, BuilderSupplier<T> valueSupplier) {
            this.valueSupplier = valueSupplier;
            ArrayList<String> entirePath = new ArrayList<>(List.of(path.split("/")));
            String configType = modId + "/" + entirePath.remove(0);
            VALUE_HOLDERS.computeIfAbsent(Pair.of(configType, new ConfigPath(entirePath.toArray(new String[]{}))), (p) -> new ArrayList<>()).add(this);
        }

        public void set(T t) {
            config.set(t);
        }

        public ForgeConfigSpec.ConfigValue<T> getConfig() {
            return config;
        }

        public T getConfigValue() {
            return config.get();
        }
    }

    public interface BuilderSupplier<T> {
        ForgeConfigSpec.ConfigValue<T> createBuilder(ForgeConfigSpec.Builder builder);
    }

    public record ConfigPath(String... strings) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ConfigPath otherPath = (ConfigPath) o;
            return Arrays.equals(strings, otherPath.strings);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(strings);
        }
    }
}