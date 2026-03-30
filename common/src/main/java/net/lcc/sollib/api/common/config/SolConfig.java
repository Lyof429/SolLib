package net.lcc.sollib.api.common.config;

import java.util.function.Supplier;

public class SolConfig {
    private final String name;
    private final double version;
    private final Supplier<JsonBuilder> builder;

    public SolConfig(String name, double version, Supplier<JsonBuilder> builder) {
        this.name = name;
        this.version = version;
        this.builder = builder;

        SolConfigRegistry.register(this);
    }

    public void init() {
        this.init(false);
    }

    protected void init(boolean force) {

    }

    public String getName() {
        return this.name;
    }
}
