package net.lcc.sollib.api.config;

import net.lcc.sollib.SolLib;

import java.util.ArrayDeque;
import java.util.Stack;

public class ConfigBuilder {
    private final StringBuilder builder;
    private final String name;
    private int indent;
    private final Stack<String> path;
    private final ArrayDeque<String> comments;

    public ConfigBuilder(String name, double version) {
         this.name = name;
         this.builder = new StringBuilder("{");
         this.indent = 0;
         this.path = new Stack<>();
         this.comments = new ArrayDeque<>();

         this.push("solconfig")
                 .add("version", version)
                 .add("force_reset", false)
                 .pop()
                 .comment("")
                 .comment("This config file uses a custom defined parser. That's why there are comments here, they wouldn't be valid in any other .json file")
                 .comment("  To add a comment yourself, just start a line with // like here");
    }

    public String build() {
        return this.builder.append("\n}").toString();
    }

    public String getCurrentPath() {
        return String.join(".", this.path);
    }

    protected void newLine(boolean comma) {
        if (this.builder.isEmpty()) return;

        char last = this.builder.charAt(this.builder.length() - 1);
        if (comma && last != '{' && last != '[')
            this.builder.append(',');
        this.builder.append('\n');

        this.builder.append("  ".repeat(this.indent + 1));
        while (!this.comments.isEmpty()) {
            String comment = this.comments.removeFirst();
            if (!comment.isEmpty())
                this.builder.append("// ");
            this.builder.append(comment).append("\n").append("  ".repeat(this.indent + 1));
        }
    }

    public ConfigBuilder comment(String comment) {
        this.comments.addLast(comment);
        return this;
    }

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
    }

    public ConfigBuilder bind(ConfigEntry entry) {
        SolLib.LOG.info(this.getCurrentPath());
        return this;
    }

    public <T> ConfigBuilder add(String key, T value) {
        this.path.push(key);
        this.newLine(true);
        this.builder.append("\"").append(key).append("\": ").append(Configurable.convert(value));
        this.path.pop();
        return this;
    }

    public ConfigBuilder.List addList(String entry) {
        this.path.push(entry);
        this.newLine(false);
        this.indent++;
        this.builder.append("\"").append(entry).append("\": [");
        return new ConfigBuilder.List();
    }

    public class List {
        public ConfigBuilder pop() {
            ConfigBuilder self = ConfigBuilder.this;
            self.path.pop();
            self.indent--;
            self.newLine(false);
            self.builder.append("]");
            return self;
        }

        public ConfigBuilder.List bind(ConfigEntry entry) {
            SolLib.LOG.info(ConfigBuilder.this.getCurrentPath());
            return this;
        }

        public <T> ConfigBuilder.List add(T value) {
            ConfigBuilder self = ConfigBuilder.this;
            self.newLine(true);
            self.builder.append(Configurable.convert(value));
            return this;
        }
    }
}
