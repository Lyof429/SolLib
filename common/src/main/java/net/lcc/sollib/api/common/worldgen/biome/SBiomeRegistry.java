package net.lcc.sollib.api.common.worldgen.biome;

import com.google.gson.JsonObject;
import net.lcc.sollib.api.common.logger.SolLogger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

    @ApiStatus.Internal
    public void read(JsonObject json) {
        String dimension = GsonHelper.getAsString(json, "dimension");
        if (!instances.containsKey(dimension))
            instances.put(dimension, this.makeGenerator(dimension));
        instances.get(dimension).read(json);
    }

    @ApiStatus.Internal
    public void iterate(Consumer<ResourceLocation> consumer) {
        instances.forEach((id, generator) -> consumer.accept(new ResourceLocation(id)));
    }

    @ApiStatus.Internal
    public void clean() {
        instances.clear();
    }

    @ApiStatus.Internal
    public JsonObject applyBiomes(ResourceLocation dimension, JsonObject biomes) {
        LOG.warn(dimension, instances);

        DimensionGenerator generator = instances.get(dimension.toString());
        LOG.warn(generator);
        if (generator == null) return biomes;

        LOG.warn(biomes);
        generator.applyBiomes(biomes.getAsJsonObject("generator").getAsJsonObject("biome_source"));
        return biomes;
    }

    @ApiStatus.Internal
    public JsonObject applyRules(ResourceLocation dimension, JsonObject noiseSettings) {
        DimensionGenerator generator = instances.get(dimension.toString());
        if (generator == null) return noiseSettings;

        generator.applyRules(noiseSettings.getAsJsonObject("surface_rule"));
        return noiseSettings;
    }
}
