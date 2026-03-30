package net.lcc.sollib.api.common.config;

import net.lcc.sollib.SolLib;

import java.util.ArrayDeque;
import java.util.Stack;
import java.util.function.Consumer;

public class ConfigBuilder {
    private final StringBuilder builder;
    private int indent;
    private final Stack<String> path;
    private String lastPath;
    private final ArrayDeque<String> comments;
    private boolean first;

    public ConfigBuilder(double version) {
         this.builder = new StringBuilder();
         this.indent = 0;
         this.path = new Stack<>();
         this.lastPath = "";
         this.comments = new ArrayDeque<>();
         this.first = true;

         this.builder.append("version: ").append(version).append("\nforce_reset: ").append((false))
                 .append("\n\n// This config file uses a custom defined parser.")
                 .append("\n//   That's why there are comments here and stray values above, they wouldn't be valid in any other .json file")
                 .append("\n//   To add a comment yourself, just start a line with // like here");
    }

    public String build(String name) {
        return this.builder.append("\n").toString();
    }

    protected void jump(boolean comma) {
        if (this.first) comma = false;
        this.first = false;

        char last = this.builder.charAt(this.builder.length() - 1);
        if (comma && last != '{' && last != '[')
            this.builder.append(',');
        this.builder.append('\n');

        this.builder.append("  ".repeat(this.indent));
        while (!this.comments.isEmpty()) {
            String comment = this.comments.removeFirst();
            if (!comment.isEmpty())
                this.builder.append("// ");
            this.builder.append(comment).append("\n").append("  ".repeat(this.indent));
        }
    }

    public ConfigBuilder comment(String comment) {
        this.comments.addLast(comment);
        return this;
    }

    public ConfigBuilder bind(ConfigEntry entry) {
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

    public ConfigBuilder entry(String key, Configurable value) {
        this.append(key, value.toConfigEntry());
        return this;
    }

    public ConfigBuilder entry(String key, String value) {
        this.append(key, "\"" + value + "\"");
        return this;
    }

    public ConfigBuilder entry(String key, Number value) {
        this.append(key, value.toString());
        return this;
    }

    public ConfigBuilder entry(String key, Boolean value) {
        this.append(key, value.toString());
        return this;
    }

    public ConfigBuilder category(String key, Consumer<ConfigBuilder> consumer) {
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

    public ConfigBuilder list(String key, Consumer<ConfigBuilder.List> consumer) {
        this.path.push(key);
        this.jump(true);
        this.indent++;
        this.builder.append("\"").append(key).append("\": [");
        consumer.accept(new ConfigBuilder.List());

        this.path.pop();
        this.indent--;
        this.jump(false);
        this.builder.append("}");

        return this;
    }

    public class List {
        protected void append(String value) {
            ConfigBuilder self = ConfigBuilder.this;

            self.jump(true);
            self.builder.append(value);
        }

        public ConfigBuilder.List entry(Configurable value) {
            this.append(value.toConfigEntry());
            return this;
        }

        public ConfigBuilder.List entry(String value) {
            this.append("\"" + value + "\"");
            return this;
        }

        public ConfigBuilder.List entry(Number value) {
            this.append(value.toString());
            return this;
        }

        public ConfigBuilder.List entry(Boolean value) {
            this.append(value.toString());
            return this;
        }

        public ConfigBuilder.List category(Consumer<ConfigBuilder> consumer) {
            ConfigBuilder self = ConfigBuilder.this;

            self.jump(true);
            self.indent++;
            self.builder.append("{");
            consumer.accept(self);

            self.indent--;
            self.jump(false);
            self.builder.append("}");

            return this;
        }

        public ConfigBuilder.List list(Consumer<ConfigBuilder.List> consumer) {
            ConfigBuilder self = ConfigBuilder.this;

            self.jump(true);
            self.indent++;
            self.builder.append("[");
            consumer.accept(new ConfigBuilder.List());

            self.indent--;
            self.jump(false);
            self.builder.append("]");

            return this;
        }
    }
}
