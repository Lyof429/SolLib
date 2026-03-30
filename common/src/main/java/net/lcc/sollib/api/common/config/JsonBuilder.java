package net.lcc.sollib.api.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.lcc.sollib.SolLib;

import java.util.ArrayDeque;
import java.util.Stack;
import java.util.function.Consumer;

public class JsonBuilder {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

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
    private String lastPath;
    private final ArrayDeque<String> comments;
    private boolean first;

    public JsonBuilder() {
         this.builder = new StringBuilder();
         this.indent = 0;
         this.path = new Stack<>();
         this.lastPath = "";
         this.comments = new ArrayDeque<>();
         this.first = true;

         /*
         this.builder.append("version: ").append(version).append("\nforce_reset: ").append((false))
                 .append("\n\n// This config file uses a custom defined parser.")
                 .append("\n//   That's why there are comments here and stray values above, they wouldn't be valid in any other .json file")
                 .append("\n//   To add a comment yourself, just start a line with // like here");*/
    }

    public String toString() {
        return "{\n" + this.builder + "\n}";
    }

    public JsonElement toJson() {
        return JsonBuilder.toJson(this.toString());
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
        SolLib.LOG.info(this.lastPath);
        return this;
    }

    protected void append(String key, String value) {
        this.path.push(key);
        this.lastPath = String.join(".", this.path);
        this.jump(true);
        this.builder.append("\"").append(key).append("\": ").append(value);
        this.path.pop();
    }

    public JsonBuilder add(String key, Configurable value) {
        String[] json = value.toConfigEntry().toString().split("\n");
        this.path.push(key);

        this.jump(true);
        this.builder.append("\"").append(key).append("\": ");

        this.builder.append(json[0]).append("\n");
        for (int i = 1; i < json.length; i++) {
            this.builder.append("  ".repeat(this.indent + 1)).append(json[i]);
            if (i != json.length - 1)
                this.builder.append("\n");
        }

        this.lastPath = String.join(".", this.path);
        this.path.pop();
        return this;
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

    public JsonBuilder addCategory(String key, Consumer<JsonBuilder> consumer) {
        this.path.push(key);
        this.lastPath = String.join(".", this.path);

        if (this.indent == 0 && this.builder.length() > 1)
            this.comment("").comment(key.toUpperCase().replace('_', ' '));
        this.jump(true);

        this.indent++;
        this.builder.append("\"").append(key).append("\": {");
        consumer.accept(this);

        this.path.pop();
        this.indent--;
        this.jump(false);
        this.builder.append("}");

        return this;
    }

    public JsonBuilder addList(String key, Consumer<JsonBuilder.List> consumer) {
        this.path.push(key);
        this.jump(true);
        this.indent++;
        this.builder.append("\"").append(key).append("\": [");
        consumer.accept(new JsonBuilder.List());

        this.path.pop();
        this.indent--;
        this.jump(false);
        this.builder.append("]");

        return this;
    }

    public class List {
        protected void append(String value) {
            JsonBuilder self = JsonBuilder.this;

            self.jump(true);
            self.builder.append(value);
        }

        public JsonBuilder.List add(Configurable value) {
            String[] json = value.toConfigEntry().toString().split("\n");
            JsonBuilder self = JsonBuilder.this;
            self.jump(true);

            self.builder.append(json[0]).append("\n");
            for (int i = 1; i < json.length; i++) {
                self.builder.append("  ".repeat(self.indent + 1)).append(json[i]);
                if (i != json.length - 1)
                    self.builder.append("\n");
            }

            return this;
        }

        public JsonBuilder.List add(String value) {
            this.append("\"" + value + "\"");
            return this;
        }

        public JsonBuilder.List add(Number value) {
            this.append(value.toString());
            return this;
        }

        public JsonBuilder.List add(Boolean value) {
            this.append(value.toString());
            return this;
        }

        public JsonBuilder.List addCategory(Consumer<JsonBuilder> consumer) {
            JsonBuilder self = JsonBuilder.this;

            self.jump(true);
            self.indent++;
            self.builder.append("{");
            consumer.accept(self);

            self.indent--;
            self.jump(false);
            self.builder.append("}");

            return this;
        }

        public JsonBuilder.List addList(Consumer<JsonBuilder.List> consumer) {
            JsonBuilder self = JsonBuilder.this;

            self.jump(true);
            self.indent++;
            self.builder.append("[");
            consumer.accept(new JsonBuilder.List());

            self.indent--;
            self.jump(false);
            self.builder.append("]");

            return this;
        }
    }
}
