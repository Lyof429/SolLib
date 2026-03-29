package net.lcc.sollib.api.config;

import net.lcc.sollib.SolLib;

import java.util.ArrayDeque;
import java.util.Stack;
import java.util.function.Consumer;

public class ConfigBuilder {
    private final StringBuilder builder;
    private final String name;
    private int indent;
    private final Stack<String> path;
    private final ArrayDeque<String> comments;
    private boolean first;

    public ConfigBuilder(String name, double version) {
         this.name = name;
         this.builder = new StringBuilder();
         this.indent = 0;
         this.path = new Stack<>();
         this.comments = new ArrayDeque<>();
         this.first = true;

         this.builder.append("version: ").append(version).append("\nforce_reset: ").append((false))
                 .append("\n\n// This config file uses a custom defined parser. That's why there are comments here, they wouldn't be valid in any other .json file")
                 .append("\n//   To add a comment yourself, just start a line with // like here");
    }

    public String build() {
        return this.builder.append("\n").toString();
    }

    public String getCurrentPath() {
        return String.join(".", this.path);
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
/*
    public ConfigBuilder push(String entry) {
        if (this.indent == 0 && this.builder.length() > 1)
            this.comment("").comment(entry.toUpperCase().replace('_', ' '));
        this.newLine(true);

        this.path.push(entry);
        this.indent++;
        this.builder.append("\"").append(entry).append("\": {");
        return this;
    }

    public ConfigBuilder pop() {
        if (this.indent <= 0)
            throw new IllegalStateException("Tried to pop a closed ConfigBuilder:\n" + this.builder.toString());

        this.path.pop();
        this.indent--;
        this.newLine(false);
        this.builder.append("}");
        return this;
    }*/

    public ConfigBuilder bind(ConfigEntry entry) {
        SolLib.LOG.info(this.getCurrentPath());
        return this;
    }

    protected void append(String key, String value) {
        this.path.push(key);
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
        public ConfigBuilder.List bind(ConfigEntry entry) {
            SolLib.LOG.info(ConfigBuilder.this.getCurrentPath());
            return this;
        }

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
