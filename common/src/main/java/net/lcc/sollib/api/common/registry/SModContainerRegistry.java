package net.lcc.sollib.api.common.registry;

import net.lcc.sollib.api.common.registry.holder.Holder;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SModContainerRegistry {
    protected final Map<String, SolModContainer> INSTANCES = new HashMap<>();

    /**
     * Stores a SolModContainer for automatic registry. Called automatically in {@link SolModContainer#SolModContainer(String name, String namespace)}
     */
    public void register(SolModContainer mod) {
        INSTANCES.putIfAbsent(mod.getNamespace(), mod);
    }

    /**
     * @return The SolModContainer for the given namespace, or null if none exists
     */
    public SolModContainer get(String id) {
        return INSTANCES.get(id);
    }

    @ApiStatus.Internal
    public <T, H extends Holder<T>> void apply(Class<H> clazz, Consumer<H> consumer) {
        for (SolModContainer mod : INSTANCES.values())
            if (mod.hasRegistrar(clazz)) mod.getRegistrar(clazz).apply(consumer);
    }
}
