package net.lcc.sollib.api.common.registry;

import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SolRegistrar<T, H extends SHolder<T>> {
    private final SolModContainer mod;
    protected final Map<String, H> instances;
    private final Constructor<H> constructor;

    public SolRegistrar(SolModContainer mod, Class<H> clazz) {
        Constructor<H> constructor = null;
        try {
            constructor = clazz.getConstructor(SolModContainer.class, String.class, Supplier.class);
        } catch (Exception ignored) {}

        this.instances = new HashMap<>();
        this.mod = mod;
        this.constructor = constructor;
    }

    public H register(String name, Supplier<T> supplier) {
        try {
            H holder = this.constructor.newInstance(this.mod, name, supplier);
            instances.putIfAbsent(name, holder);
            return holder;
        } catch (Exception ignored) {
            return null;
        }
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