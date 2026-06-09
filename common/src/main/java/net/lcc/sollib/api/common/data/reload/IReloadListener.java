package net.lcc.sollib.api.common.data.reload;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lcc.sollib.api.common.data.runtime.RuntimeData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.Map;

/**
 * Interface for resource reload listeners. <br/>
 * All instances must be registered with {@link SReloadRegistry#register(IReloadListener)} to have an effect. <br/>
 * This is a {@link FunctionalInterface} for the sake of syntax sugar, where calls such as this one are possible:
 *
 * <pre>
 * {@code
 *     SolReloadRegistry.register(manager -> {
 *         SolLib.MOD.getLogger().info("Hello World!");
 *     });
 * }
 * </pre> <br/>
 *
 * {@link #preload(ResourceManager)} is called on every instance after config reloading but before vanilla reloading.
 * Typically, this is a safe place to set up your {@link RuntimeData}. <br/>
 * {@link #reload(ResourceManager)} is called on every instance after vanilla reloading.
 */
@FunctionalInterface
public interface IReloadListener {
    default void preload(ResourceManager manager) {}
    void reload(ResourceManager manager);


    static JsonObject open(Map.Entry<ResourceLocation, Resource> entry) {
        return open(entry.getKey(), entry.getValue());
    }

    static JsonObject open(ResourceLocation id, Resource resource) {
        try {
            String content = new String(resource.open().readAllBytes());
            JsonElement json = new Gson().fromJson(content, JsonElement.class);

            if (json == null || !json.isJsonObject()) return null;

            return json.getAsJsonObject();
        } catch (Throwable e) {
            SReloadRegistry.LOG.error("Could not read data at " + id);
        }
        return null;
    }
}
