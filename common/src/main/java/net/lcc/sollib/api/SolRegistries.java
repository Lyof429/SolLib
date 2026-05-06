package net.lcc.sollib.api;

import net.lcc.sollib.api.common.config.SConfigRegistry;
import net.lcc.sollib.api.common.data.reload.SReloadRegistry;
import net.lcc.sollib.api.common.data.runtime.SRuntimeRegistry;
import net.lcc.sollib.api.common.registry.SModContainerRegistry;
import net.lcc.sollib.api.common.weather.SWeatherCommandRegistry;

public class SolRegistries {
    public static final SConfigRegistry CONFIG = new SConfigRegistry();
    public static class Data {
        public static final SReloadRegistry RELOAD = new SReloadRegistry();
        public static final SRuntimeRegistry RUNTIME = new SRuntimeRegistry();
    }
    public static final SModContainerRegistry MOD = new SModContainerRegistry();
    public static final SWeatherCommandRegistry WEATHER = new SWeatherCommandRegistry();
}
