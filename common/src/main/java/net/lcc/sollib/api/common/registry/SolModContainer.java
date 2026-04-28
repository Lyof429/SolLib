package net.lcc.sollib.api.common.registry;

import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.config.IConfigurable;
import net.lcc.sollib.api.common.config.SolConfig;
import net.lcc.sollib.api.common.logger.SolLogger;
import net.lcc.sollib.api.common.registry.holder.Holder;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class SolModContainer {
    private final String name;
    private final String namespace;

    private final SolLogger logger;
    private final Map<Class<?>, SolRegistrar<?, ?>> registrars;
    private final Map<String, SolConfig> configs;

    public SolModContainer(String name, String namespace) {
        this.name = name;
        this.namespace = namespace;

        this.logger = new SolLogger(this.name);
        this.registrars = new HashMap<>();
        this.configs = new HashMap<>();

        SolRegistries.MOD.register(this);
    }

    public String getName() {
        return this.name;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public ResourceLocation makeID(String name) {
        return ResourceLocation.tryBuild(this.getNamespace(), name);
    }

    public SolLogger getLogger() {
        return this.logger;
    }

    public boolean hasRegistrar(Class<?> clazz) {
        return this.registrars.containsKey(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T, H extends Holder<T>> SolRegistrar<T, H> getRegistrar(Class<H> clazz) {
        if (this.registrars.containsKey(clazz))
            return (SolRegistrar<T, H>) this.registrars.get(clazz);

        SolRegistrar<T, H> r = new SolRegistrar<>(this, clazz);
        this.registrars.put(clazz, r);
        return r;
    }

    public SolConfig createConfig(String name, double version, IConfigurable contentBuilder) {
        SolConfig c = new SolConfig(name, version, contentBuilder);
        c.init();
        this.configs.putIfAbsent(name, c);
        return c;
    }

    public SolConfig getConfig(String name) {
        return this.configs.get(name);
    }

    public Iterable<SolConfig> getConfigs() {
        return this.configs.values();
    }
}
