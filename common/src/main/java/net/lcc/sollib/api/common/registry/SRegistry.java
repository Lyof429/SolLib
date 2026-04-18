package net.lcc.sollib.api.common.registry;

import net.lcc.sollib.SolLib;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SRegistry<T extends Holder<?>> {
    protected final Map<String, T> instances = new HashMap<>();
    protected final Constructor<T> constructor;

    public SRegistry(Class<T> clazz) {
        Constructor<T> constructor = null;
        try {
            constructor = clazz.getConstructor(Supplier.class);
        } catch (Exception ignored) {}
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