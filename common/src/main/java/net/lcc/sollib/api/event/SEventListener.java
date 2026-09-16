package net.lcc.sollib.api.event;

import net.lcc.sollib.api.common.config.SolConfig;
import net.lcc.sollib.api.common.data.reload.IReloadListener;
import net.lcc.sollib.api.common.data.runtime.RuntimeData;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.function.Consumer;

public interface SEventListener {
    /**
     * Pass each of your custom {@link SEventType}s to this to register them to the system <br/>
     * No need to implement if you only want to *use* SEvents
     */
    default void registerEvents(Consumer<SEventType<?>> registrar) {}

    /**
     * Called everytime a {@link SolConfig} is built. Can be used to append extra fields to its end.
     */
    default void onConfigBuild(SolConfig.BuildEvent event) {}

    /**
     * <b>Use in place of {@link IReloadListener} if possible</b> <br>
     * Called on every instance after vanilla reloading.
     */
    default void onReload(ResourceManager manager) {}

    /**
     * <b>Use in place of {@link IReloadListener} if possible</b> <br>
     * Called after config reloading but before vanilla reloading. <br>
     * Typically, this is a safe place to set up your {@link RuntimeData}.
     */
    default void onPreload(ResourceManager manager) {}
}
