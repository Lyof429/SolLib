package net.lcc.sollib.api.common;

import net.lcc.sollib.api.common.config.SConfigRegistry;
import net.lcc.sollib.api.common.data.reload.SReloadRegistry;
import net.lcc.sollib.api.common.data.runtime.SRuntimeRegistry;
import net.lcc.sollib.api.common.registry.SModContainerRegistry;
import net.lcc.sollib.api.common.weather.SWeatherCommandRegistry;

public class SolRegistries {
    public static final SConfigRegistry CONFIG = SConfigRegistry.INSTANCE;
    public static class Data {
        public static final SReloadRegistry RELOAD = SReloadRegistry.INSTANCE;
        public static final SRuntimeRegistry RUNTIME = SRuntimeRegistry.INSTANCE;
    }
    public static final SModContainerRegistry MOD = SModContainerRegistry.INSTANCE;
    public static final SWeatherCommandRegistry WEATHER = SWeatherCommandRegistry.INSTANCE;
}
