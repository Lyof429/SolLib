package net.lcc.sollib.api.common.registry;

import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

public class SModContainerRegistry {
    protected final List<SolModContainer> INSTANCES = new ArrayList<>();

    /**
     * Stores a SolModContainer for automatic registry. Called automatically in {@link SolModContainer#SolModContainer(String name, String namespace)}
     */
    public void register(SolModContainer mod) {
        INSTANCES.add(mod);
    }

    @ApiStatus.Internal
    public <T, H extends Holder<T>> void apply(Class<H> clazz, IRegistryConsumer<T> consumer) {
        for (SolModContainer mod : INSTANCES)
            if (mod.hasRegistrar(clazz)) mod.getRegistrar(clazz).apply(consumer);
    }
}
