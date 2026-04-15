package net.lcc.sollib.api.common.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lcc.sollib.SolLib;

import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigEntry<T> implements Supplier<T> {
    private String[] path;
    private T cache;
    private T fallback;
    private Function<JsonElement, T> processor;

    public ConfigEntry(T fallback) {
        this((SolConfig) null, "", fallback);
    }

    public ConfigEntry(String configId, String path, T fallback) {
        this(SolConfigRegistry.get(configId), path, fallback);
    }

    public ConfigEntry(SolConfig config, String path, T fallback) {
        this.set(config, path, fallback);
        this.fallback = fallback;
        this.processor = null;
    }

    protected void set(SolConfig config, String path, T fallback) {
        if (config != null) config.addEntry(path, this);
        this.path = path.split("\\.");
        this.cache = null;
        /*this.fallback = fallback;*/
    }

    public ConfigEntry<T> withProcessor(Function<JsonElement, T> processor) {
        this.processor = processor;
        return this;
    }

    public T reload(JsonElement elm) {
        this.cache = null;

        JsonObject obj;
        for (String s : this.path) {
            if (!elm.isJsonObject())
                return this.fail();

            obj = elm.getAsJsonObject();
            if (!obj.has(s))
                return this.fail();

            elm = obj.get(s);
        }

        this.convert(elm);
        return this.cache;
    }

    public T get() {
        return this.cache == null ? this.fallback : this.cache;
    }

    @SuppressWarnings("unchecked")
    protected void convert(JsonElement result) {
        try {
            if (this.processor != null) this.cache = this.processor.apply(result);

            else if (this.fallback instanceof Integer) this.cache = (T) (Integer) result.getAsInt();
            else if (this.fallback instanceof Double) this.cache = (T) (Double) result.getAsDouble();
            else if (this.fallback instanceof Boolean) this.cache = (T) (Boolean) result.getAsBoolean();
            else if (this.fallback instanceof String) this.cache = (T) result.getAsString();
            else if (this.fallback instanceof JsonObject) this.cache = (T) result.getAsJsonObject();
            else if (this.fallback instanceof JsonArray) this.cache = (T) result.getAsJsonArray();
        } catch (Exception ignored) {
            this.fail();
        }
    }

    protected T fail() {
        this.cache = this.fallback;
        SolLib.LOG.info("Could not find config value for path:", String.join(".", this.path));
        return this.cache;
    }
}
