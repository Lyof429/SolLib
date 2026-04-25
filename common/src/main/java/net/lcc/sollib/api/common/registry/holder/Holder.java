package net.lcc.sollib.api.common.registry.holder;

import net.lcc.sollib.api.common.registry.SolModContainer;

import java.util.function.Supplier;

public class Holder<T> implements Supplier<T> {
    protected final SolModContainer mod;
    protected final String name;

    private T cachedEntry;
    private final Supplier<T> entrySupplier;

    public Holder(SolModContainer mod, String name, Supplier<T> entrySupplier) {
        this.mod = mod;
        this.name = name;
        this.entrySupplier = entrySupplier;
    }

    /**
     * Retrieves the cached entry if it exists, otherwise calls the supplier to create a new entry.
     * @return The cached entry, or a new entry if the cached entry does not exist.
     */
    public T get() {
        if (this.cachedEntry != null) return cachedEntry;

        T entry = entrySupplier.get();
        this.cachedEntry = entry;

        return entry;
    }
}
