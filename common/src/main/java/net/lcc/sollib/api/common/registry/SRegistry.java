package net.lcc.sollib.api.common.registry;

import java.util.HashMap;
import java.util.Map;

public class SRegistry<T extends Holder<?>> {
    protected final Map<String, T> INSTANCES = new HashMap<>();

    public T register(String name, T holder) {
        INSTANCES.putIfAbsent(name, holder);
        return holder;
    }
}