package net.lcc.sollib.api.common.config;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.platform.Services;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class SolConfig {
    public static String fromJson(String json) {
        /*StringBuilder builder = new StringBuilder();
        builder.append("version: ").append(this.version).append("\nforce_reset: ").append((false))
                .append("\n\n// This config file uses a custom defined parser.")
                .append("\n//   That's why there are comments here and stray values above, they wouldn't be valid in any other .json file")
                .append("\n//   To add a comment yourself, just start a line with // like here");*/
        return json;
    }

    public static String toJson(String json) {
        return json;
    }


    private final String name;
    private final double version;
    private final Consumer<JsonBuilder> builder;

    public SolConfig(String name, double version, Consumer<JsonBuilder> builder) {
        this.name = name;
        this.version = version;
        this.builder = builder;

        SolConfigRegistry.register(this);
    }

    public void init() {
        this.init(false);
    }

    protected void init(boolean force) {
        Path path = Services.PLATFORM.getConfigDirectory();

        for (String dir : this.getSuffixName().split("/")) {
            path = path.resolve(dir);

            if (!path.endsWith(".json") && !Files.exists(path)) {
                try {
                    Files.createDirectory(path);
                } catch (IOException e) {
                    SolLib.LOG.error(this.getName(), ": Error while accessing config file\n", e);
                    return;
                }
            }
        }

        File file = path.toFile();
        boolean create = !file.isFile();

        JsonBuilder builder = new JsonBuilder(this);
        this.builder.accept(builder);
        String content = builder.toString();

        try {
            if (create || force) {
                file.delete();
                file.createNewFile();

                FileWriter writer = new FileWriter(file);
                writer.write(SolConfig.fromJson(content));
                writer.close();

                SolLib.LOG.debug(this.getName(), ": Config file created");
            }

            content = SolConfig.toJson(FileUtils.readFileToString(file, StandardCharsets.UTF_8));

        } catch (IOException e) {
            SolLib.LOG.error(this.getName(), ": Error while creating config file\n", e);
            return;
        }

        SolLib.LOG.info(content);
    }

    public String getName() {
        return this.name;
    }

    public String getSuffixName() {
        return this.name + ".json";
    }
}
