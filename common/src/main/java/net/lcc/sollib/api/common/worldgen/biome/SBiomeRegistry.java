package net.lcc.sollib.api.common.worldgen.biome;

import com.google.gson.JsonObject;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.logger.SolLogger;
import net.lcc.sollib.core.Identifier;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class SBiomeRegistry {
    public static final SBiomeRegistry INSTANCE = new SBiomeRegistry();
    private SBiomeRegistry() {}

    protected static final SolLogger LOG = new SolLogger("SolLib/WorldGen/Biome");

    private final Map<String, DimensionGenerator> instances = new HashMap<>();

    /**
     * Abstracted constructor for {@link DimensionGenerator}.
     * Allows using a different generator logic depending on the target dimension.
     * This is meant to be mixin'd into to fit your needs
     * @param dimension The id of the target dimension (e.g. minecraft:the_end)
     * @return An instance of a DimensionGenerator or subclass
     */
    protected DimensionGenerator makeGenerator(String dimension) {
        return new DimensionGenerator();
    }

    /**
     * @return true iff custom generation is registered for the given dimension id
     */
    public boolean has(String dimension) {
        return instances.containsKey(dimension);
    }

    @ApiStatus.Internal
    public void apply(ResourceManager manager, BiFunction<ResourceLocation, Resource, JsonObject> reader) {
        instances.clear();

        FileToIdConverter.json("sollib/worldgen/biome").listMatchingResources(manager).forEach((id, resource) -> {
            JsonObject json = reader.apply(id, resource);
            String dimension = GsonHelper.getAsString(json, "dimension");
            if (!instances.containsKey(dimension))
                instances.put(dimension, this.makeGenerator(dimension));
            instances.get(dimension).read(json);
        });

        instances.forEach((name, generator) -> {
            ResourceLocation id = Identifier.of(name);

            ResourceLocation dimension = Identifier.of(id.getNamespace(), "dimension/" + id.getPath() + ".json");
            SolRegistries.Data.RUNTIME.addJson(dimension, json -> SolRegistries.BIOME.applyBiomes(id, json))
                    .withEphemeral(true);

            manager.getResource(dimension).ifPresent(resource -> {
                JsonObject obj = reader.apply(dimension, resource);
                obj = GsonHelper.getAsJsonObject(obj, "generator");
                ResourceLocation settings = Identifier.of(GsonHelper.getAsString(obj, "settings"));

                SolRegistries.Data.RUNTIME.addJson(
                        Identifier.of(settings.getNamespace(), "worldgen/noise_settings/" + settings.getPath() + ".json"),
                        json -> SolRegistries.BIOME.applyRules(id, json)).withEphemeral(true);
            });
        });
    }

    @ApiStatus.Internal
    public JsonObject applyBiomes(ResourceLocation dimension, JsonObject biomes) {
        DimensionGenerator generator = instances.get(dimension.toString());
        if (generator == null) return biomes;

        biomes.getAsJsonObject("generator").add("biome_source",
                generator.applyBiomes(biomes.getAsJsonObject("generator").getAsJsonObject("biome_source")));
        return biomes;
    }

    @ApiStatus.Internal
    public JsonObject applyRules(ResourceLocation dimension, JsonObject noiseSettings) {
        DimensionGenerator generator = instances.get(dimension.toString());
        if (generator == null) return noiseSettings;

        noiseSettings.add("surface_rule",
                generator.applyRules(noiseSettings.getAsJsonObject("surface_rule")));
        return noiseSettings;
    }
}
