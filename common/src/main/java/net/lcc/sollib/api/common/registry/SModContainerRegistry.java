package net.lcc.sollib.api.common.registry;

import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SModContainerRegistry {
    public static final SModContainerRegistry INSTANCE = new SModContainerRegistry();
    private SModContainerRegistry() {}

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

    /**
     * Loops over the {@link SolRegistrar}{@literal <T, H>} from every {@link SolModContainer},
     * and applies <b>consumer</b> to every {@link Holder}{@literal <T>}
     */
    public <T, H extends Holder<T>> void iterate(Class<H> clazz, Consumer<H> consumer) {
        for (SolModContainer mod : INSTANCES.values())
            if (mod.hasRegistrar(clazz)) mod.getRegistrar(clazz).iterate(consumer);
    }

    /**
     * Loops over the {@link SolRegistrar}{@literal <T, H>} from every {@link SolModContainer},
     * and returns the first {@link Holder}{@literal <T>} that verifies <b>predicate</b>
     */
    public <T, H extends Holder<T>> H find(Class<H> clazz, Predicate<H> predicate) {
        H result = null;
        for (SolModContainer mod : INSTANCES.values()) {
            if (mod.hasRegistrar(clazz)) result = mod.getRegistrar(clazz).find(predicate);
            if (result != null) return result;
        }
        return null;
    }
}
