package net.lcc.sollib.api.common.config;

import net.lcc.sollib.SolLib;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SConfigRegistry {
    protected final Map<String, SolConfig> INSTANCES = new HashMap<>();

    /**
     * @param name The name of the config to get
     * @return The corresponding SolConfig instance, or null
     */
    @Nullable
    public SolConfig get(String name) {
        return INSTANCES.get(name);
    }

    /**
     * Automatically called by any SolConfig in its constructor
     * @param config The instance of SolConfig to register
     */
    public void register(SolConfig config) {
        INSTANCES.put(config.getName(), config);
    }

    /**
     * Reloads every config registered, reading their file and fetching the required ConfigEntry <br/>
     * Automatically called on resource reload
     */
    @ApiStatus.Internal
    public void reload() {
        SolLib.LOG.info("Loaded", INSTANCES.size(), "configs");
        for (SolConfig config : INSTANCES.values())
            config.init();
    }
}
