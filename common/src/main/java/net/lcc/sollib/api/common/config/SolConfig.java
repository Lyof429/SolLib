package net.lcc.sollib.api.common.config;

import com.google.common.util.concurrent.AtomicDouble;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.SolRegistries;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class SolConfig {
    /**
     * Converts {@code json} from a JSON to a SolConfig formatted string
     */
    public static String fromJson(String json, double version) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n// This config file uses a custom defined parser.")
                .append("\n//   That's why there are comments here and stray values below, they wouldn't be valid in any other .json file")
                .append("\n//   To add a comment yourself, just start a line with // like here")
                .append("\n\nversion: ").append(version).append("\nreset: ").append(false);

        boolean started = false;
        for (String line : json.split("\n")) {
            if (line.startsWith("{")) started = true;
            else if (started) {
                if (line.startsWith("}")) started = false;
                else builder.append("\n").append(line.substring(2));
            }
        }

        return builder.append("\n").toString();
    }

    /**
     * Converts {@code json} from a SolConfig formatted string to a JSON
     */
    public static String toJson(String json, AtomicDouble version, AtomicBoolean reset) {
        StringBuilder builder = new StringBuilder("{");

        for (String line : json.split("\n")) {
            if (line.startsWith("version")) {
                try {
                    version.set(Double.parseDouble(line.split(":")[1].strip()));
                } catch (Exception ignored) {}
            } else if (line.startsWith("reset")) {
                try {
                    reset.set(Boolean.parseBoolean(line.split(":")[1].strip()));
                } catch (Exception ignored) {}
            }

            else builder.append("\n  ").append(line);
        }

        return builder.append("\n}").toString();
    }


    private final String name;
    private final double version;
    private final IConfigurable contentBuilder;
    private JsonElement content;
    private LoadType loadResult;
    private final Map<String, ConfigEntry<?>> entries;

    public SolConfig(String name, double version, IConfigurable contentBuilder) {
        this.name = name;
        this.version = version;
        this.contentBuilder = contentBuilder;
        this.content = new JsonObject();
        this.loadResult = LoadType.GOOD;
        this.entries = new HashMap<>();

        SolRegistries.CONFIG.register(this);
    }

    public void init() {
        this.init(false);
    }

    public void init(boolean force) {
        this.content = new JsonObject();
        this.loadResult = LoadType.GOOD;

        Path path = Services.PLATFORM.getConfigDirectory();

        for (String dir : this.getSuffixName().split("/")) {
            path = path.resolve(dir);

            if (!path.endsWith(".json") && !Files.exists(path)) {
                try {
                    Files.createDirectory(path);
                } catch (IOException e) {
                    SolLib.LOG.error(this.getName(), ": Error while accessing config file\n", e);
                    this.loadResult = LoadType.ERROR;
                }
            }
        }

        IJsonBuilder builder = new JsonBuilder(this);
        this.contentBuilder.toJson(builder);
        String content = builder.toString();
        AtomicDouble version = new AtomicDouble(this.version);
        AtomicBoolean reset = new AtomicBoolean(false);

        // Only continue processing if the file was accessed correctly
        if (this.loadResult == LoadType.GOOD) {
            File file = path.toFile();
            boolean create = !file.isFile();

            try {
                if (create || force) {
                    file.delete();
                    file.createNewFile();

                    FileWriter writer = new FileWriter(file);
                    writer.write(SolConfig.fromJson(content, this.version));
                    writer.close();

                    SolLib.LOG.debug(this.getName(), ": Config file created");
                }

                content = SolConfig.toJson(FileUtils.readFileToString(file, StandardCharsets.UTF_8), version, reset);

            } catch (IOException e) {
                SolLib.LOG.error(this.getName(), ": Error while creating config file\n", e);
                this.loadResult = LoadType.ERROR;
            }

            if (reset.get()) this.init(true);

            if (this.loadResult == LoadType.GOOD && this.version > version.get())
                this.loadResult = LoadType.OUTDATED;
        }

        try {
            this.content = JsonBuilder.toJson(content);
        } catch (Exception e) {
            SolLib.LOG.error(this.getName(), ": Error while reading config file\n", e);
            this.loadResult = LoadType.ERROR;
        }

        if (this.loadResult.message != null)
            SolLib.LOG.warn(this.getName(), ":", this.loadResult.message);

        for (ConfigEntry<?> entry : this.entries.values())
            entry.withContent(this.content);
    }

    public void openFile() {
        try {
            Path path = Services.PLATFORM.getConfigDirectory();
            for (String dir : this.getSuffixName().split("/"))
                path = path.resolve(dir);

            Util.getPlatform().openFile(path.toFile());
        } catch (Exception e) {
            SolLib.LOG.error(this.getName(), ": Error while opening config file\n", e);
        }
    }

    public String getName() {
        return this.name;
    }

    public String getSuffixName() {
        return this.name + ".sol.json";
    }

    public LoadType getLoadResult() {
        return this.loadResult;
    }

    protected void addEntry(String path, ConfigEntry<?> entry) {
        entry.withContent(this.content);
        this.entries.remove(path);
        this.entries.put(path, entry);
    }

    public <T> T get(String path, T fallback) {
        if (this.entries.containsKey(path))
            return (T) this.entries.get(path).get();

        ConfigEntry<T> entry = new ConfigEntry<>(this, path, fallback).withContent(this.content);
        this.addEntry(path, entry);
        return entry.get();
    }

    public <T> JsonElement getRaw(String path, T fallback) {
        if (this.entries.containsKey(path))
            return this.entries.get(path).getRaw();

        ConfigEntry<T> entry = new ConfigEntry<>(this, path, fallback).withContent(this.content);
        this.addEntry(path, entry);
        return entry.getRaw();
    }
}
