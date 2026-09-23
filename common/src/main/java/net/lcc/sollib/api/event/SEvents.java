package net.lcc.sollib.api.event;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.common.config.SolConfig;
import net.lcc.sollib.platform.Dependency;
import net.lcc.sollib.platform.Services;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public interface SEvents {
    SEventType<SolConfig.BuildEvent> ON_CONFIG_BUILD = new SEventType<>(listener -> listener::onConfigBuild);
    SEventType<ResourceManager> ON_RELOAD = new SEventType<>(listener -> listener::onReload);
    SEventType<ResourceManager> ON_PRELOAD = new SEventType<>(listener -> listener::onPreload);

    @ApiStatus.Internal
    static void load() {
        List<SEventListener> listeners = new ArrayList<>();

        Iterator<SEventListener> iterator = ServiceLoader.load(SEventListener.class).iterator();
        while (iterator.hasNext()) {
            try {
                SEventListener loadedService = iterator.next();

                Dependency dependency = loadedService.getClass().getAnnotation(Dependency.class);
                String id = dependency == null ? null : dependency.mod();

                if (id != null && !Services.PLATFORM.isModLoaded(id))
                    continue;

                listeners.add(loadedService);

            } catch (Throwable e) {
                SolLib.MOD.getLogger().error(e);
            }
        }

        for (SEventListener listener : listeners)
            listener.registerEvents(event -> listeners.forEach(event::addListener));
    }
}
