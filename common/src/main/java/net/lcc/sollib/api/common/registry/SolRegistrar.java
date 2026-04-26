package net.lcc.sollib.api.common.registry;

import net.lcc.sollib.api.common.registry.holder.Holder;
import net.minecraft.core.Registry;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SolRegistrar<T, H extends Holder<T>> {
    protected final SolModContainer mod;
    protected final Map<String, H> instances;
    protected final Constructor<H> constructor;
    protected final Registry<T> registry;

    public SolRegistrar(SolModContainer mod, Class<H> clazz) {
        Constructor<H> constructor = null;
        try {
            constructor = clazz.getConstructor(SolModContainer.class, String.class, Supplier.class);
        } catch (Exception ignored) {}
        Registry<T> registry = null;
        try {
            registry = (Registry<T>) clazz.getMethod("getRegistryType").invoke(null);
        } catch (Exception ignored) {}

        this.instances = new HashMap<>();
        this.mod = mod;
        this.constructor = constructor;
        this.registry = registry;
    }

    public H register(String name, Supplier<T> supplier) {
        try {
            return this.register(name, this.constructor.newInstance(this.mod, name, supplier));
        } catch (Exception ignored) {
            return null;
        }
    }

    public H register(String name, H holder) {
        instances.putIfAbsent(name, holder);
        return holder;
    }

    public H get(String name) {
        return this.instances.getOrDefault(name, null);
    }

    @ApiStatus.Internal
    public void iterate(Consumer<H> consumer) {
        for (H entry : this.instances.values())
            consumer.accept(entry);
    }

    @ApiStatus.Internal
    public H find(Predicate<H> predicate) {
        for (H entry : this.instances.values())
            if (predicate.test(entry)) return entry;
        return null;
    }
}