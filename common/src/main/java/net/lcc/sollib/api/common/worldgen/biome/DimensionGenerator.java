package net.lcc.sollib.api.common.worldgen.biome;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

public class DimensionGenerator {
    public record BiomeData(ResourceLocation id, JsonObject data) {}

    protected final List<BiomeData> noise;
    protected final List<BiomeData> rules;

    public DimensionGenerator() {
        this.noise = new ArrayList<>();
        this.rules = new ArrayList<>();
    }

    public void read(JsonObject json) {

    }

    public void applyBiomes(JsonObject target) {
        SBiomeRegistry.LOG.info(target);
    }

    public void applyRules(JsonObject target) {
        SBiomeRegistry.LOG.info(target);
    }
}
