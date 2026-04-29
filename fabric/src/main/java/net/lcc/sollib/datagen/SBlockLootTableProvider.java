package net.lcc.sollib.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.data.block.BlockModel;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;

import java.util.Map;

public class SBlockLootTableProvider extends FabricBlockLootTableProvider {
    public SBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        SolRegistries.MOD.iterate(BlockHolder.class, holder -> {
            for (Map.Entry<BlockModel, BlockHolder> entry : holder.getBlockSet().entrySet()) {
                if (entry.getValue().hasDrop()) continue;

                switch (entry.getKey()) {
                    case SLAB -> {
                        add(entry.getValue().get(), createSlabItemTable(entry.getValue().get()));
                    }
                    case DOOR -> {
                        add(entry.getValue().get(), createDoorTable(entry.getValue().get()));
                    }
                    default -> {
                        add(entry.getValue().get(), createSingleItemTable(entry.getValue().get()));
                    }
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
}