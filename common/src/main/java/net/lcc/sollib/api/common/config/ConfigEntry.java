package net.lcc.sollib.api.common.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.SolRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigEntry<T> implements Supplier<T> {
    private JsonElement content;
    private String[] path;
    private JsonElement jsoncache;
    private T cache;
    private T fallback;
    private Function<JsonElement, T> processor;
    private boolean shouldLog;

    public ConfigEntry(T fallback) {
        this((SolConfig) null, "", fallback);
    }

    public ConfigEntry(String configId, String path, T fallback) {
        this(SolRegistries.CONFIG.get(configId), path, fallback);
    }

    public ConfigEntry(SolConfig config, String path, T fallback) {
        this.content = null;
        this.set(config, path, fallback);
        this.fallback = fallback;
        this.processor = null;
        this.shouldLog = false;
    }

    public void set(SolConfig config, String path, T fallback) {
        if (config != null)
            config.addEntry(path, this);
        this.path = path.split("\\.");
        /*this.fallback = fallback;*/
    }

    public ConfigEntry<T> withProcessor(Function<JsonElement, T> processor) {
        this.processor = processor;
        return this;
    }

    public ConfigEntry<T> withContent(JsonElement elm) {
        this.jsoncache = null;
        this.cache = null;
        this.content = elm;
        return this;
    }

    public ConfigEntry<T> withLogging(boolean shouldLog) {
        this.shouldLog = shouldLog;
        return this;
    }

    public T get() {
        if (this.cache != null) return this.cache;
        if (this.content == null) return this.fallback;

        this.jsoncache = this.getRaw();

        this.convert(this.jsoncache);
        return this.cache;
    }

    public JsonElement getRaw() {
        if (this.jsoncache != null) return this.jsoncache;
        if (this.content == null) return null;

        JsonElement elm = this.content;
        JsonObject obj;
        for (String s : this.path) {
            if (!elm.isJsonObject())
                return this.fail();

            obj = elm.getAsJsonObject();
            if (!obj.has(s))
                return this.fail();

            elm = obj.get(s);
        }

        return elm;
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

            else this.cache = this.fallback;
        } catch (Exception ignored) {
            this.fail();
        }
    }

    protected JsonElement fail() {
        if (this.shouldLog)
            SolLib.LOG.info("Could not find config value for path:", String.join(".", this.path));
        return null;
    }
}
