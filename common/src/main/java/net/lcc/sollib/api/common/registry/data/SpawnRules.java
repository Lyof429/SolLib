package net.lcc.sollib.api.common.registry.data;

import com.google.gson.JsonObject;
import net.lcc.sollib.api.common.config.builder.JsonBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public record SpawnRules(List<ResourceKey<Biome>> biomeKeys, List<TagKey<Biome>> biomeTags,
                         MobCategory category, int weight, int min, int max) {

    public static SpawnRules create(List<String> biomes, MobCategory category, int weight, int min, int max) {
        List<ResourceKey<Biome>> biomeKeys = new ArrayList<>();
        List<TagKey<Biome>> biomeTags = new ArrayList<>();
        for (String s : biomes) {
            if (s.startsWith("#"))
                biomeTags.add(TagKey.create(Registries.BIOME, new ResourceLocation(s.substring(1))));
            else biomeKeys.add(ResourceKey.create(Registries.BIOME, new ResourceLocation(s)));
        }
        return new SpawnRules(biomeKeys, biomeTags, category, weight, min, max);
    }

    public boolean matchesBiome(Predicate<ResourceKey<Biome>> isBiome, Predicate<TagKey<Biome>> isTag) {
        for (ResourceKey<Biome> key : this.biomeKeys())
            if (isBiome.test(key)) return true;
        for (TagKey<Biome> key : this.biomeTags())
            if (isTag.test(key)) return true;
        return false;
    }

    public JsonObject createBiomeModifier(JsonObject json, ResourceLocation entity) {
        // Not modifying spawns if they already exist
        if (json != null) return json;

        // Generating the biome modifier file
        return new JsonBuilder()
                .add("type", "forge:add_spawns")
                .add("biomes", "#" + entity + "_can_spawn")
                .addObject("spawners", spawners -> spawners
                        .add("type", entity.toString())
                        .add("weight", this.weight())
                        .add("minCount", this.min())
                        .add("maxCount", this.max())
                ).toJson();
    }

    public JsonObject createTag(JsonObject json, ResourceLocation entity) {
        if (json != null) return json;

        return new JsonBuilder()
                .add("replace", false)
                .addArray("values", biomes -> {
                    for (ResourceKey<Biome> key : this.biomeKeys())
                        biomes.add(key.location().toString());
                    for (TagKey<Biome> key : this.biomeTags())
                        biomes.add("#" + key.location());
                }).toJson();
    }
}
