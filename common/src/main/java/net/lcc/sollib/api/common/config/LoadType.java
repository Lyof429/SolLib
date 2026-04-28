package net.lcc.sollib.api.common.config;

public enum LoadType {
    GOOD(null, 0xff404040),
    OUTDATED("Outdated config! Consider resetting it!", 0xff8B8660),
    ERROR("This config failed to load! Consider fixing or resetting it!", 0xff8b4040);

    public final String message;
    public final int color;

    LoadType(String message, int color) {
        this.message = message;
        this.color = color;
    }
}
