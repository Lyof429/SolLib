package net.lcc.sollib.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.EntityHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class SEntityTagProvider extends FabricTagProvider.EntityTypeTagProvider{
    public SEntityTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        SolRegistries.MOD.iterate(EntityHolder.class, holder -> {
            for (TagKey<EntityType<?>> tag : holder.getTags())
                getOrCreateTagBuilder(tag).add(holder.get());
        });
    }
}