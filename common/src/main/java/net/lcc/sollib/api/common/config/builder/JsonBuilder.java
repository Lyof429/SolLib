package net.lcc.sollib.api.common.config.builder;

import com.google.gson.*;
import com.google.gson.stream.MalformedJsonException;
import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.config.SolConfig;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

public class JsonBuilder implements IJsonBuilder {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * @return The JSON represented by the given String
     * @throws MalformedJsonException If the given String has wrong syntax
     */
    public static JsonElement toJson(String json) throws MalformedJsonException {
        StringBuilder result = new StringBuilder();

        for (String line : json.split("\n")) {
            if (!line.strip().startsWith("//"))
                result.append(line).append("\n");
        }

        return GSON.fromJson(result.toString(), JsonElement.class);
    }


    private final StringBuilder builder;
    private int indent;
    private final Stack<String> path;
    private final ArrayDeque<String> comments;
    private boolean first;

    private SolConfig config;
    private String currentPath;
    private Object currentValue;

    /**
     * Instantiates a JsonBuilder configured for the creation of a SolConfig <br/>
     * Allows directly binding a ConfigEntry to the config
     */
    public JsonBuilder(SolConfig config) {
        this();
        this.config = config;
    }

    public JsonBuilder() {
        this.builder = new StringBuilder();
        this.indent = 0;
        this.path = new Stack<>();
        this.comments = new ArrayDeque<>();
        this.first = true;

        this.config = null;
        this.currentPath = "";
        this.currentValue = null;
    }

    /**
     * @return A String representation of the JSON this built, with pretty printing
     */
    @Override
    public String toString() {
        return "{\n" + this.builder + "\n}";
    }

    /**
     * @return The JSON this built
     */
    @Override
    public JsonObject toJson() {
        try {
            return JsonBuilder.toJson(this.toString()).getAsJsonObject();
        } catch (MalformedJsonException e) {
            return new JsonObject();
        }
    }

    /**
     * {@link #bind(ConfigEntry)} should instead be used to directly bind a ConfigEntry to this path
     * @return The current config path this builder is at
     */
    @Override
    public String getCurrentPath() {
        return this.currentPath;
    }

    protected void jump(boolean comma) {
        if (this.first) {
            if (this.config != null) this.comment("");
            comma = false;
        }
        this.first = false;

        if (!this.builder.isEmpty()) {
            char last = this.builder.charAt(this.builder.length() - 1);
            if (comma && last != '{' && last != '[')
                this.builder.append(',');
            this.builder.append('\n');
        }

        this.builder.append("  ".repeat(this.indent + 1));
        while (!this.comments.isEmpty()) {
            String comment = this.comments.removeFirst();
            if (!comment.isEmpty())
                this.builder.append("// ");
            this.builder.append(comment).append("\n").append("  ".repeat(this.indent + 1));
        }
    }

    @Override
    public IJsonBuilder comment(String comment) {
        this.comments.addLast(comment);
        return this;
    }

    @Override
    public <T> IJsonBuilder bind(ConfigEntry<T> entry) {
        if (this.config != null && entry != null)
            entry.withLogging(true).set(this.config, this.getCurrentPath(), (T) this.currentValue);

        return this;
    }

    protected void append(String key, String value) {
        this.path.push(key);
        this.currentPath = String.join(".", this.path);
        this.jump(true);
        this.builder.append("\"").append(key).append("\": ").append(value);
        this.path.pop();
    }

    @Override
    public IJsonBuilder add(String key, String value) {
        this.append(key, "\"" + value + "\"");
        this.currentValue = value;
        return this;
    }

    @Override
    public IJsonBuilder add(String key, Number value) {
        this.append(key, value.toString());
        this.currentValue = value;
        return this;
    }

    @Override
    public IJsonBuilder add(String key, boolean value) {
        this.append(key, String.valueOf(value));
        this.currentValue = value;
        return this;
    }

    @Override
    public IJsonBuilder addObject(String key, IConfigurable consumer) {
        this.path.push(key);
        this.currentPath = String.join(".", this.path);
        this.currentValue = new JsonObject();

        if (this.indent == 0 && this.config != null)
            this.comment(key.toUpperCase().replace('_', ' '));
        this.jump(true);

        this.indent++;
        this.builder.append("\"").append(key).append("\": {");
        consumer.toJson(this);

        this.currentPath = String.join(".", this.path);
        this.currentValue = new JsonObject();
        this.path.pop();
        this.indent--;
        this.jump(false);
        this.builder.append("}");

        return this;
    }

    @Override
    public IJsonBuilder addObject(String key, JsonObject value) {
        return this.addObject(key, self -> {
            for (String k : value.keySet()) {
                JsonElement v = value.get(k);
                if (v.isJsonPrimitive()) {
                    JsonPrimitive p = v.getAsJsonPrimitive();

                    if (p.isString()) self.add(k, p.getAsString());
                    else if (p.isNumber()) self.add(k, p.getAsDouble());
                    else if (p.isBoolean()) self.add(k, p.getAsBoolean());
                } else if (v.isJsonObject())
                    self.addObject(k, v.getAsJsonObject());
                else if (v.isJsonArray())
                    self.addArray(k, v.getAsJsonArray());
            }
        });
    }

    @Override
    public IJsonBuilder addArray(String key, Consumer<IArrayBuilder> consumer) {
        this.path.push(key);
        this.currentPath = String.join(".", this.path);
        this.currentValue = new JsonArray();

        this.jump(true);
        this.indent++;
        this.builder.append("\"").append(key).append("\": [");
        consumer.accept(new ArrayBuilder());

        this.path.pop();
        this.indent--;
        this.jump(false);
        this.builder.append("]");

        return this;
    }

    @Override
    public IJsonBuilder addArray(String key, Iterable<?> value) {
        return this.addArray(key, self -> {
            for (Object v : value) {
                if (v instanceof String it) self.add(it);
                else if (v instanceof Number it) self.add(it);
                else if (v instanceof Boolean it) self.add(it);
                else if (v instanceof IConfigurable it) self.addObject(it);
            }
        });
    }

    public class ArrayBuilder implements IArrayBuilder {
        protected void append(String value) {
            JsonBuilder self = JsonBuilder.this;

            self.jump(true);
            self.builder.append(value);
        }

        @Override
        public IArrayBuilder add(String value) {
            this.append("\"" + value + "\"");
            return this;
        }

        @Override
        public IArrayBuilder add(Number value) {
            this.append(value.toString());
            return this;
        }

        @Override
        public IArrayBuilder add(Boolean value) {
            this.append(value.toString());
            return this;
        }

        @Override
        public IArrayBuilder addObject(IConfigurable consumer) {
            JsonBuilder self = JsonBuilder.this;

            self.jump(true);
            self.indent++;
            self.builder.append("{");
            consumer.toJson(self);

            self.indent--;
            self.jump(false);
            self.builder.append("}");

            return this;
        }

        @Override
        public IArrayBuilder addObject(JsonObject value) {
            return this.addObject(self -> {
                for (String k : value.keySet()) {
                    JsonElement v = value.get(k);
                    if (v.isJsonPrimitive()) {
                        JsonPrimitive p = v.getAsJsonPrimitive();

                        if (p.isString()) self.add(k, p.getAsString());
                        else if (p.isNumber()) self.add(k, p.getAsDouble());
                        else if (p.isBoolean()) self.add(k, p.getAsBoolean());
                    } else if (v.isJsonObject())
                        self.addObject(k, v.getAsJsonObject());
                    else if (v.isJsonArray())
                        self.addArray(k, v.getAsJsonArray());
                }
            });
        }

        @Override
        public IArrayBuilder addArray(Consumer<IArrayBuilder> consumer) {
            JsonBuilder self = JsonBuilder.this;

            self.jump(true);
            self.indent++;
            self.builder.append("[");
            consumer.accept(new ArrayBuilder());

            self.indent--;
            self.jump(false);
            self.builder.append("]");

            return this;
        }

        @Override
        public IArrayBuilder addArray(Iterable<?> value) {
            return this.addArray(self -> {
                for (Object v : value) {
                    if (v instanceof String it) self.add(it);
                    else if (v instanceof Number it) self.add(it);
                    else if (v instanceof Boolean it) self.add(it);
                    else if (v instanceof IConfigurable it) self.addObject(it);
                }
            });
        }
    }
}
