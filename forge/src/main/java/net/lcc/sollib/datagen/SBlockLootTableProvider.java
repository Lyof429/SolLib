package net.lcc.sollib.datagen;

import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.data.block.BlockModel;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.Set;

public class SBlockLootTableProvider extends BlockLootSubProvider {

    public SBlockLootTableProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        SolRegistries.MOD.iterate(BlockHolder.class, holder -> {
            for (Map.Entry<BlockModel, BlockHolder> entry : holder.getBlockSet().entrySet()) {
                if (entry.getValue().hasDrop()) continue;

                switch (entry.getKey()) {
                    case SLAB -> add(entry.getValue().get(), createSlabItemTable(entry.getValue().get()));
                    case DOOR -> add(entry.getValue().get(), createDoorTable(entry.getValue().get()));
                    default -> add(entry.getValue().get(), createSingleItemTable(entry.getValue().get()));
                }
            }

            if (holder.hasDrop()) {
                if (holder.getDropCount() == null)
                    add(holder.get(), createSilkTouchOnlyTable(holder.getDrop().get()));
                else
                    add(holder.get(), createSingleItemTable(holder.getDrop().get(), holder.getDropCount()));
            }
        });
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        java.util.List<Block> blocks = new java.util.ArrayList<>();
        SolRegistries.MOD.iterate(BlockHolder.class, holder -> {
            for (Map.Entry<BlockModel, BlockHolder> entry : holder.getBlockSet().entrySet()) {
                if (!entry.getValue().hasDrop()) {
                    blocks.add(entry.getValue().get());
                }
            }
            if (holder.hasDrop()) {
                blocks.add(holder.get());
            }
        });
        return blocks;
    }
}