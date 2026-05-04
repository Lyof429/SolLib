package net.lcc.sollib.api.common.config.builder;

import com.google.gson.JsonObject;
import net.lcc.sollib.api.common.config.ConfigEntry;

import java.util.function.Consumer;

public interface IJsonBuilder {
    String toString();
    JsonObject toJson();

    String getCurrentPath();
    IJsonBuilder comment(String comment);
    <T> IJsonBuilder bind(ConfigEntry<T> entry);

    IJsonBuilder add(String key, String value);
    IJsonBuilder add(String key, Number value);
    IJsonBuilder add(String key, boolean value);
    IJsonBuilder addObject(String key, IConfigurable value);
    IJsonBuilder addObject(String key, JsonObject value);
    IJsonBuilder addArray(String key, Consumer<IArrayBuilder> consumer);
    IJsonBuilder addArray(String key, Iterable<?> value);

    interface IArrayBuilder {
        IArrayBuilder add(String value);
        IArrayBuilder add(Number value);
        IArrayBuilder add(Boolean value);
        IArrayBuilder addObject(IConfigurable consumer);
        IArrayBuilder addObject(JsonObject value);
        IArrayBuilder addArray(Consumer<IArrayBuilder> consumer);
        IArrayBuilder addArray(Iterable<?> value);
    }
}
