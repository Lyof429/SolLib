package net.lcc.sollib.datagen;

import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.EntityHolder;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SEntityLootTableProvider extends EntityLootSubProvider {
    public SEntityLootTableProvider() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        SolRegistries.MOD.iterate(EntityHolder.class, holder -> {
            if (holder.hasDrop())
                add(holder.get(), holder.getDrop().get());
        });
    }
    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        List<EntityType<?>> entities = new ArrayList<>();
        SolRegistries.MOD.iterate(EntityHolder.class, holder -> {
            if (holder.hasDrop()) {
                entities.add(holder.get());
            }
        });
        return entities.stream();
    }
}