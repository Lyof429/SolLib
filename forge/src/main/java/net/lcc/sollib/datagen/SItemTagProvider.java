package net.lcc.sollib.datagen;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SItemTagProvider extends ItemTagsProvider {

    public SItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, SolLib.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        SolRegistries.MOD.iterate(ItemHolder.class, holder -> {
            for (TagKey<Item> tag : holder.getTags())
                this.tag(tag).add(holder.get());
        });
    }
}