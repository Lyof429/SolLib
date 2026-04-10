package net.lcc.sollib.api.common.config;

import net.lcc.sollib.SolLib;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SolConfigRegistry {
    protected static final Map<String, SolConfig> CONFIG_MAP = new HashMap<>();

    /**
     * @param name The name of the config to get
     * @return The corresponding SolConfig instance, or null
     */
    @Nullable
    public static SolConfig get(String name) {
        return CONFIG_MAP.get(name);
    }

    /**
     * @param config The instance of SolConfig to register <br/>
     * Automatically called by any SolConfig in its constructor
     */
    public static void register(SolConfig config) {
        CONFIG_MAP.put(config.getName(), config);
    }

    /**
     * Reloads every config registered, reading their file and fetching the required ConfigEntry <br/>
     * Automatically called on resource reload
     */
    public static void reload() {
        SolLib.LOG.info("Loaded", CONFIG_MAP.size(), "configs");
        for (SolConfig config : CONFIG_MAP.values())
            config.init();
    }
}
