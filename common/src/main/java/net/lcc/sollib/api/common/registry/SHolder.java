package net.lcc.sollib.api.common.registry;

import net.lcc.sollib.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class SHolder<T> implements Supplier<T> {
    protected SolModContainer mod;
    protected String name;

    private T cachedEntry;
    private final Supplier<T> entrySupplier;

    public SHolder(SolModContainer mod, String name, Supplier<T> entrySupplier) {
        this.mod = mod;
        this.name = name;
        this.entrySupplier = entrySupplier;

        if (Services.PLATFORM.getPlatformName().equals("Fabric") && this.getRegistry() != null)
            Registry.register(this.getRegistry(), this.getID(), this.get());
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

    /**
     * @return This entry's identifier, built from its name and mod container
     */
    public ResourceLocation getID() {
        return this.mod.makeID(this.name);
    }

    public Registry<T> getRegistry() {
        return null;
    }
}
