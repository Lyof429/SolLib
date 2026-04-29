package net.lcc.sollib.api.common.registry.data;

import net.lcc.sollib.SolLib;
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
        SolLib.LOG.info("attempting to spawn");
        for (ResourceKey<Biome> key : this.biomeKeys())
            if (isBiome.test(key)) return true;
        for (TagKey<Biome> key : this.biomeTags())
            if (isTag.test(key)) return true;
        return false;
    }

    public Iterable<String> iterateBiomes() {
        List<String> r = new ArrayList<>();
        for (ResourceKey<Biome> key : this.biomeKeys())
            r.add("#" + key.location());
        for (TagKey<Biome> key : this.biomeTags())
            r.add(key.location().toString());
        return r;
    }
}
