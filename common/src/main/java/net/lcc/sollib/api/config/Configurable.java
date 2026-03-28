package net.lcc.sollib.api.config;

public interface Configurable {
    String toConfigEntry();

    static <T> String convert(T value) throws IllegalArgumentException {
        if (value instanceof Configurable it)
            return it.toConfigEntry();
        if (value instanceof String)
            return "\"" + value + "\"";
        if (value instanceof Number || value instanceof Boolean)
            return String.valueOf(value);
        throw new IllegalArgumentException("value must be a JsonPrimitive or Configurable");
    }
}
