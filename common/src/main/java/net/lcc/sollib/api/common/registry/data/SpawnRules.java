package net.lcc.sollib.api.common.registry.data;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Predicate;

public record SpawnRules(Predicate<Biome> biomes, MobCategory category, int weight, int min, int max) {
}
