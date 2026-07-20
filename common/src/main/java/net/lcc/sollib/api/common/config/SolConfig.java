package net.lcc.sollib.api.common.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.config.builder.IConfigurable;
import net.lcc.sollib.api.common.config.builder.IJsonBuilder;
import net.lcc.sollib.api.common.config.builder.JsonBuilder;
import net.lcc.sollib.platform.Services;
import net.minecraft.Util;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SolConfig {
    /**
     * Converts {@code json} from a JSON to a SolConfig formatted string
     */
    public static String fromJson(String json, Content content) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n// This config file uses a custom defined parser.")
                .append("\n//   That's why there are comments here and stray values below, they wouldn't be valid in any other .json file")
                .append("\n//   To add a comment yourself, just start a line with // like here")
                .append("\n\nversion: ").append(content.version).append("\nreset: ").append(false);

        boolean started = false;
        for (String line : json.split("\n")) {
            if (line.startsWith("{")) started = true;
            else if (started) {
                if (line.startsWith("}")) started = false;
                else {
                    if (line.length() >= 2) builder.append("\n").append(line.substring(2));
                    else builder.append("\n").append(line);
                }
            }
        }

        return builder.append("\n").toString();
    }

    /**
     * Converts {@code json} from a SolConfig formatted string to a JSON
     */
    public static String toJson(Content content) {
        StringBuilder builder = new StringBuilder("{");
        boolean b = true;

        for (String line : content.text.split("\n")) {
            if (b) b = false;
            else builder.append("\n");

            if (line.startsWith("version:")) {
                try {
                    content.version = Double.parseDouble(line.split(":")[1].strip());
                } catch (Exception ignored) {}
            } else if (line.startsWith("reset:")) {
                try {
                    content.reset = Boolean.parseBoolean(line.split(":")[1].strip());
                } catch (Exception ignored) {}
            }

            else builder.append("  ").append(line);
        }

        return builder.append("\n}").toString();
    }


    private final String name;
    private final double version;
    private final IConfigurable contentBuilder;
    private final Content content;
    private final Map<String, ConfigEntry<?>> entries;

    public SolConfig(String name, double version, IConfigurable contentBuilder) {
        this.name = name;
        this.version = version;
        this.contentBuilder = contentBuilder;
        this.content = new Content();
        this.entries = new HashMap<>();

        SolRegistries.CONFIG.register(this);
    }

    public void init() {
        this.init(false);
    }

    public void init(boolean force) {
        this.content.json = new JsonObject();
        this.content.result = LoadResult.GOOD;

        Path path = Services.PLATFORM.getConfigDirectory();

        for (String dir : this.getSuffixName().split("/")) {
            path = path.resolve(dir);

            if (!path.endsWith(".json") && !Files.exists(path)) {
                try {
                    Files.createDirectory(path);
                } catch (IOException e) {
                    SConfigRegistry.LOG.error(this.getName(), ": Error while accessing config file\n", e);
                    this.content.result = LoadResult.ERROR;
                }
            }
        }

        IJsonBuilder builder = new JsonBuilder(this);
        this.contentBuilder.toJson(builder);
        String json = builder.toString();
        this.content.text = SolConfig.fromJson(json, this.content);
        this.content.version = this.version;

        // Only continue processing if the file was accessed correctly
        if (this.content.result == LoadResult.GOOD) {
            File file = path.toFile();
            boolean create = !file.isFile();

            try {
                if (create || force) {
                    file.delete();
                    file.createNewFile();

                    FileWriter writer = new FileWriter(file);
                    writer.write(this.content.text);
                    writer.close();

                    SConfigRegistry.LOG.info(this.getName(), ": Config file created");
                }

                this.content.text = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                json = SolConfig.toJson(this.content);

            } catch (IOException e) {
                SConfigRegistry.LOG.error(this.getName(), ": Error while creating config file\n", e);
                this.content.result = LoadResult.ERROR;
            }

            if (this.content.reset) this.init(true);

            if (this.content.result == LoadResult.GOOD && this.version > this.content.version)
                this.content.result = LoadResult.OUTDATED;
        }

        try {
            this.content.json = JsonBuilder.toJson(json);
        } catch (Exception e) {
            SConfigRegistry.LOG.error(this.getName(), ": Error while reading config file\n", e);
            this.content.result = LoadResult.ERROR;
        }

        if (this.content.result.message != null)
            SConfigRegistry.LOG.warn(this.getName(), ":", this.content.result.message);

        for (ConfigEntry<?> entry : this.entries.values())
            entry.withContent(this.content.json);
    }

    public void openFile() {
        try {
            Path path = Services.PLATFORM.getConfigDirectory();
            for (String dir : this.getSuffixName().split("/"))
                path = path.resolve(dir);

            Util.getPlatform().openFile(path.toFile());
        } catch (Exception e) {
            SConfigRegistry.LOG.error(this.getName(), ": Error while opening config file\n", e);
        }
    }

    public void writeFile(String text) {
        try {
            Path path = Services.PLATFORM.getConfigDirectory();
            for (String dir : this.getSuffixName().split("/"))
                path = path.resolve(dir);

            File file = path.toFile();

            file.delete();
            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write(text);
            writer.close();

            SConfigRegistry.LOG.info(this.getName(), ": Config file created");
            this.init();
        } catch (Exception e) {
            SConfigRegistry.LOG.error(this.getName(), ": Error while opening config file\n", e);
        }
    }

    public String getName() {
        return this.name;
    }

    public String getSuffixName() {
        return this.name + ".sol.json";
    }

    public Content getContent() {
        return this.content;
    }

    protected void addEntry(String path, ConfigEntry<?> entry) {
        entry.withContent(this.content.json);
        this.entries.remove(path);
        this.entries.put(path, entry);
    }

    public <T> T get(String path, T fallback) {
        if (this.entries.containsKey(path))
            return (T) this.entries.get(path).get();

        ConfigEntry<T> entry = new ConfigEntry<>(this, path, fallback).withContent(this.content.json);
        this.addEntry(path, entry);
        return entry.get();
    }

    public <T> JsonElement getRaw(String path, T fallback) {
        if (this.entries.containsKey(path))
            return this.entries.get(path).getRaw();

        ConfigEntry<T> entry = new ConfigEntry<>(this, path, fallback).withContent(this.content.json);
        this.addEntry(path, entry);
        return entry.getRaw();
    }


    public static class Content {
        public LoadResult result = LoadResult.GOOD;
        public String text = "";
        public JsonElement json = new JsonObject();
        public double version = 0;
        public boolean reset = false;
    }
}
