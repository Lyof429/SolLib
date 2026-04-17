package net.lcc.sollib.api.common.data.reload;

import net.minecraft.server.packs.resources.ResourceManager;

/**
 * Interface for resource reload listeners. <br/>
 * All instances must be registered with {@link SReloadRegistry#register(IReloadListener)} to have an effect. <br/>
 * This is a {@link FunctionalInterface} for the sake of syntax sugar, where calls such as this one are possible:
 *
 * <pre>
 * {@code
 *     SolReloadRegistry.register(manager -> {
 *         SolLib.LOGGER.info("Hello World!");
 *     });
 * }
 * </pre> <br/>
 *
 * {@link #preload(ResourceManager)} is called on every instance after config reloading but before vanilla reloading.
 * Typically, this is a safe place to set up your {@link IDataFunction}. <br/>
 * {@link #reload(ResourceManager)} is called on every instance after vanilla reloading.
 */
@FunctionalInterface
public interface IReloadListener {
    default void preload(ResourceManager manager) {}
    void reload(ResourceManager manager);
}
