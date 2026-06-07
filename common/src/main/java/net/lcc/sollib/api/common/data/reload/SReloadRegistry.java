package net.lcc.sollib.api.common.data.reload;

import net.lcc.sollib.api.common.logger.SolLogger;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

public class SReloadRegistry {
    public static final SReloadRegistry INSTANCE = new SReloadRegistry();
    private SReloadRegistry() {}

    protected static final SolLogger LOG = new SolLogger("Sol/Data/Reload");

    private final List<IReloadListener> INSTANCES = new ArrayList<>();

    /**
     * @param listener An instance of a {@link IReloadListener} implementation, which will then be reloaded on every resource reload.
     */
    public void register(IReloadListener listener) {
        INSTANCES.remove(listener);
        INSTANCES.add(listener);
    }

    /**
     * Calls preload on every registered IReloadListener <br/>
     * Automatically called before resource reload
     */
    @ApiStatus.Internal
    public void preload(ResourceManager manager) {
        for (IReloadListener listener : INSTANCES) {
            try {
                listener.preload(manager);
            } catch (Exception e) {
                LOG.error(listener, ": Error while running preload", e);
            }
        }
    }

    /**
     * Calls reload on every registered IReloadListener <br/>
     * Automatically called after resource reload
     */
    @ApiStatus.Internal
    public void reload(ResourceManager manager) {
        for (IReloadListener listener : INSTANCES) {
            try {
                listener.reload(manager);
            } catch (Exception e) {
                LOG.error(listener, ": Error while running reload", e);
            }
        }
    }
}
