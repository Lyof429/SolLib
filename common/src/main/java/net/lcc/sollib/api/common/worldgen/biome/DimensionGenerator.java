package net.lcc.sollib.api.common.worldgen.biome;

import com.google.gson.JsonObject;
import net.lcc.sollib.api.common.config.builder.JsonBuilder;
import net.lcc.sollib.core.Identifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.ArrayList;
import java.util.List;

public class DimensionGenerator {
    public record BiomeData(ResourceLocation id, JsonObject data) {
        @Override
        public String toString() {
            return "BiomeData{" +
                    "id=" + id +
                    ", data=" + data +
                    '}';
        }
    }

    protected final List<BiomeData> noise;
    protected final List<BiomeData> rules;

    public DimensionGenerator() {
        this.noise = new ArrayList<>();
        this.rules = new ArrayList<>();
    }

    public void read(JsonObject json) {
        if (!json.has("biome")) return;
        ResourceLocation biome = Identifier.of(GsonHelper.getAsString(json, "biome"));

        if (json.has("parameters")) {
            this.noise.add(new BiomeData(biome, new JsonBuilder()
                    .add("biome", biome.toString())
                    .addObject("parameters", GsonHelper.getAsJsonObject(json, "parameters"))
                    .toJson()
            ));
        }

        if (json.has("surface_rule")) {
            this.rules.add(SBiomeRegistry.LOG.error(new BiomeData(biome, new JsonBuilder()
                    .add("type", "minecraft:condition")
                    .addObject("if_true", it -> it
                            .add("type", "minecraft:biome")
                            .addArray("biome_is", List.of(biome.toString()))
                    ).addObject("then_run", GsonHelper.getAsJsonObject(json, "surface_rule"))
                    .toJson()
            )));
        }
    }

    public JsonObject applyBiomes(JsonObject biomeSource) {
        for (BiomeData data : this.noise)
            biomeSource.getAsJsonArray("biomes").add(data.data());
        return biomeSource;
    }

    public JsonObject applyRules(JsonObject original) {
        SBiomeRegistry.LOG.info(this.rules);
        return new JsonBuilder()
                .add("type", "minecraft:sequence")
                .addArray("sequence", it -> {
                    for (BiomeData data : this.rules)
                        it.addObject(data.data());
                    it.addObject(original);
                }).toJson();
    }
}
