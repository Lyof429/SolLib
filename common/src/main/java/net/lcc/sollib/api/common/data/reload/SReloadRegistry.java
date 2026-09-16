package net.lcc.sollib.api.common.data.reload;

import net.lcc.sollib.api.common.logger.SolLogger;
import net.lcc.sollib.api.event.SEventListener;
import net.lcc.sollib.api.event.SEventType;
import net.lcc.sollib.api.event.SEvents;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SReloadRegistry implements SEventListener {
    public static final SReloadRegistry INSTANCE = new SReloadRegistry();

    protected static final SolLogger LOG = new SolLogger("Sol/Data/Reload");

    private final List<IReloadListener> INSTANCES = new ArrayList<>();

    /**
     * @param listener An instance of a {@link IReloadListener} implementation, which will then be reloaded on every resource reload.
     */
    public void register(IReloadListener listener) {
        INSTANCES.remove(listener);
        INSTANCES.add(listener);
    }

    @Override
    public void registerEvents(Consumer<SEventType<?>> registrar) {
        registrar.accept(SEvents.ON_RELOAD);
        registrar.accept(SEvents.ON_PRELOAD);
    }

    @Override
    public void onReload(ResourceManager manager) {
        for (IReloadListener listener : INSTANCE.INSTANCES) {
            try {
                listener.reload(manager);
            } catch (Exception e) {
                LOG.error(listener, ": Error while running reload", e);
            }
        }
    }

    @Override
    public void onPreload(ResourceManager manager) {
        for (IReloadListener listener : INSTANCE.INSTANCES) {
            try {
                listener.preload(manager);
            } catch (Exception e) {
                LOG.error(listener, ": Error while running preload", e);
            }
        }
    }
}
