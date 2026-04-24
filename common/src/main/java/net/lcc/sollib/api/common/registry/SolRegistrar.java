package net.lcc.sollib.api.common.registry;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SolRegistrar<T extends Holder<?>> {
    protected final Map<String, T> instances;
    protected final String namespace;
    protected final Constructor<T> constructor;

    public SolRegistrar(String namespace, Class<T> clazz) {
        Constructor<T> constructor = null;
        try {
            constructor = clazz.getConstructor(Supplier.class);
        } catch (Exception ignored) {}

        this.instances = new HashMap<>();
        this.namespace = namespace;
        this.constructor = constructor;
    }

    public T register(String name, Supplier<?> supplier) {
        try {
            return this.register(name, this.constructor.newInstance(supplier));
        } catch (Exception ignored) {
            return null;
        }
    }

    public T register(String name, T holder) {
        instances.putIfAbsent(name, holder);
        return holder;
    }

    public T get(String name) {
        return this.instances.getOrDefault(name, null);
    }
}