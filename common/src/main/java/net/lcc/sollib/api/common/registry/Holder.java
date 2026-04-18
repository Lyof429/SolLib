package net.lcc.sollib.api.common.registry;

import net.minecraft.data.models.model.ModelTemplate;

import java.util.function.Supplier;

public class Holder<T> implements Supplier<T> {
    private T cachedEntry;
    private final Supplier<T> entrySupplier;

    public Holder(Supplier<T> entrySupplier) {
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
