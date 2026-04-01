package net.lcc.sollib.api.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.lcc.sollib.SolLib;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

public class JsonBuilder {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * @return The JSON represented by the given String
     */
    public static JsonElement toJson(String json) {
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
    private String currentPath;
    private final ArrayDeque<String> comments;
    private boolean first;
    private SolConfig config;

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
         this.currentPath = "";
         this.comments = new ArrayDeque<>();
         this.first = true;
         this.config = null;
    }

    /**
     * @return A String representation of the JSON this built, with pretty printing
     */
    public String toString() {
        return "{\n" + this.builder + "\n}";
    }

    /**
     * @return The JSON this built
     */
    public JsonElement toJson() {
        return JsonBuilder.toJson(this.toString());
    }

    /**
     * {@link #bind(ConfigEntry)} should instead be used to directly bind a ConfigEntry to this path
     * @return The current config path this builder is at
     */
    public String getCurrentPath() {
        return this.currentPath;
    }

    protected void jump(boolean comma) {
        if (this.first) comma = false;
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

    public JsonBuilder comment(String comment) {
        this.comments.addLast(comment);
        return this;
    }

    public JsonBuilder bind(ConfigEntry entry) {
        if (this.config != null && entry != null)
            SolLib.LOG.info(this.getCurrentPath());
        return this;
    }

    protected void append(String key, String value) {
        this.path.push(key);
        this.currentPath = String.join(".", this.path);
        this.jump(true);
        this.builder.append("\"").append(key).append("\": ").append(value);
        this.path.pop();
    }

    public JsonBuilder add(String key, String value) {
        this.append(key, "\"" + value + "\"");
        return this;
    }

    public JsonBuilder add(String key, Number value) {
        this.append(key, value.toString());
        return this;
    }

    public JsonBuilder add(String key, Boolean value) {
        this.append(key, value.toString());
        return this;
    }

    public JsonBuilder addCategory(String key, Configurable consumer) {
        this.path.push(key);
        this.currentPath = String.join(".", this.path);

        if (this.indent == 0)
            this.comment("").comment(key.toUpperCase().replace('_', ' '));
        this.jump(true);

        this.indent++;
        this.builder.append("\"").append(key).append("\": {");
        consumer.toJson(this);

        this.currentPath = String.join(".", this.path);
        this.path.pop();
        this.indent--;
        this.jump(false);
        this.builder.append("}");

        return this;
    }

    public JsonBuilder addList(String key, Consumer<JsonList> consumer) {
        this.path.push(key);
        this.jump(true);
        this.indent++;
        this.builder.append("\"").append(key).append("\": [");
        consumer.accept(new JsonList());

        this.path.pop();
        this.indent--;
        this.jump(false);
        this.builder.append("]");

        return this;
    }

    public <T> JsonBuilder addList(String key, List<T> value) {
        return this.addList(key, self -> {
            for (T v : value) {
                if (v instanceof String it) self.add(it);
                else if (v instanceof Number it) self.add(it);
                else if (v instanceof Boolean it) self.add(it);
                else if (v instanceof Configurable it) self.addCategory(it);
            }
        });
    }

    public class JsonList {
        protected void append(String value) {
            JsonBuilder self = JsonBuilder.this;

            self.jump(true);
            self.builder.append(value);
        }

        public JsonList add(String value) {
            this.append("\"" + value + "\"");
            return this;
        }

        public JsonList add(Number value) {
            this.append(value.toString());
            return this;
        }

        public JsonList add(Boolean value) {
            this.append(value.toString());
            return this;
        }

        public JsonList addCategory(Configurable consumer) {
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

        public JsonList addList(Consumer<JsonList> consumer) {
            JsonBuilder self = JsonBuilder.this;

            self.jump(true);
            self.indent++;
            self.builder.append("[");
            consumer.accept(new JsonList());

            self.indent--;
            self.jump(false);
            self.builder.append("]");

            return this;
        }

        public <T> JsonList addList(List<T> value) {
            return this.addList(self -> {
                for (T v : value) {
                    if (v instanceof String it) self.add(it);
                    else if (v instanceof Number it) self.add(it);
                    else if (v instanceof Boolean it) self.add(it);
                    else if (v instanceof Configurable it) self.addCategory(it);
                }
            });
        }
    }
}
