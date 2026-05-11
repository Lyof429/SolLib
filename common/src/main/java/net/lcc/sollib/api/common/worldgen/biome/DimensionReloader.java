package net.lcc.sollib.api.common.worldgen.biome;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.data.reload.IReloadListener;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class DimensionReloader implements IReloadListener {
    @Override
    public void preload(ResourceManager manager) {
        SBiomeRegistry.LOG.info("Reloading biomes!");
        SolRegistries.BIOME.clean();

        FileToIdConverter finder = FileToIdConverter.json("sollib/worldgen/biome");
        finder.listMatchingResources(manager).forEach((id, resource) -> {
            try {
                String content = new String(resource.open().readAllBytes());
                JsonElement json = new Gson().fromJson(content, JsonElement.class);

                if (json == null || !json.isJsonObject()) return;

                SolRegistries.BIOME.read(json.getAsJsonObject());
            } catch (Throwable e) {
                SBiomeRegistry.LOG.error("Could not read data at " + id);
            }
        });

        SolRegistries.BIOME.iterate(dimension -> {
            SBiomeRegistry.LOG.info(dimension);

            SolRegistries.Data.RUNTIME.addJson(
                    ResourceLocation.tryBuild(dimension.getNamespace(), "dimension/" + dimension.getPath() + ".json"),
                    json -> SolRegistries.BIOME.applyBiomes(dimension, json)).withEphemeral(true);
            SolRegistries.Data.RUNTIME.addJson(
                    ResourceLocation.tryBuild(dimension.getNamespace(), "worldgen/noise_settings/" + dimension.getPath() + ".json"),
                    json -> SolRegistries.BIOME.applyRules(dimension, json)).withEphemeral(true);
        });
    }

    @Override
    public void reload(ResourceManager manager) {}
}
