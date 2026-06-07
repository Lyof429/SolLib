package net.lcc.sollib.api.common.registry.data.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public record SpawnRestrictions<T extends Mob>(SpawnPlacementType location, Heightmap.Types heightmap,
                                               SpawnPlacements.SpawnPredicate<T> predicate) {
}
