package net.lcc.sollib.api.common.registry;

import java.util.HashMap;
import java.util.Map;

public class SolRegistrar {
    private final String modid;
    private final Map<Class<?>, SRegistry<?>> registries;

    public SolRegistrar(String modid) {
        this.modid = modid;
        this.registries = new HashMap<>();
    }

    public String getModID() {
        return this.modid;
    }

    @SuppressWarnings("unchecked")
    public <T, H extends Holder<T>> SRegistry<H> get(Class<H> clazz) {
        if (this.registries.containsKey(clazz))
            return (SRegistry<H>) this.registries.get(clazz);

        SRegistry<H> r = new SRegistry<>(clazz);
        this.registries.put(clazz, r);
        return r;
    }
}
