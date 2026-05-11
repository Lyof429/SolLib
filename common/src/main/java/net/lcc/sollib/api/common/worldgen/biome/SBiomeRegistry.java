package net.lcc.sollib.api.common.worldgen.biome;

import com.google.gson.JsonObject;
import net.lcc.sollib.api.common.logger.SolLogger;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SBiomeRegistry {
    public static final SBiomeRegistry INSTANCE = new SBiomeRegistry();
    private SBiomeRegistry() {}

    protected static final SolLogger LOG = new SolLogger("SolLib/WorldGen/Biome");

    private final Map<String, DimensionGenerator> INSTANCES = new HashMap<>();

    /**
     * Gets or create the {@link DimensionGenerator} associated to this dimension id
     */
    public DimensionGenerator get(String dimension) {
        if (!this.has(dimension))
            INSTANCES.put(dimension, this.makeGenerator(dimension));
        return INSTANCES.get(dimension);
    }

    /**
     * @return true iff the specified dimension id has an associated {@link DimensionGenerator}
     */
    public boolean has(String dimension) {
        return INSTANCES.containsKey(dimension);
    }

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
    public void applyBiomes(ResourceLocation dimension, JsonObject biomes) {
        DimensionGenerator generator = INSTANCES.get(dimension.toString());
        if (generator == null) return;

        generator.applyBiomes(biomes.getAsJsonObject("generator").getAsJsonObject("biome_source"));
    }

    @ApiStatus.Internal
    public void applyRules(ResourceLocation dimension, JsonObject noiseSettings) {
        DimensionGenerator generator = INSTANCES.get(dimension.toString());
        if (generator == null) return;

        generator.applyRules(noiseSettings.getAsJsonObject("surface_rule"));
    }
}
