package net.lcc.sollib.datagen;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.EntityHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SEntityTagProvider extends EntityTypeTagsProvider {

    public SEntityTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, SolLib.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        SolRegistries.MOD.iterate(EntityHolder.class, holder -> {
            for (TagKey<EntityType<?>> tag : holder.getTags())
                tag(tag).add(holder.get());
        });
    }
}