package net.lcc.sollib.api;

import net.lcc.sollib.api.common.registry.Holder;
import net.lcc.sollib.api.common.registry.SolRegistrar;

import java.util.HashMap;
import java.util.Map;

public class SolModContainer {
    private final String namespace;
    private final Map<Class<?>, SolRegistrar<?>> registries;

    public SolModContainer(String namespace) {
        this.namespace = namespace;
        this.registries = new HashMap<>();
    }

    public String getNamespace() {
        return this.namespace;
    }

    @SuppressWarnings("unchecked")
    public <T, H extends Holder<T>> SolRegistrar<H> getRegistrar(Class<H> clazz) {
        if (this.registries.containsKey(clazz))
            return (SolRegistrar<H>) this.registries.get(clazz);

        SolRegistrar<H> r = new SolRegistrar<>(this.getNamespace(), clazz);
        this.registries.put(clazz, r);
        return r;
    }
}
