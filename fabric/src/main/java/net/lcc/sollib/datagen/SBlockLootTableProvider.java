package net.lcc.sollib.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.data.BlockModel;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;

import java.util.Map;
import java.util.Objects;

public class SBlockLootTableProvider extends FabricBlockLootTableProvider {
    public SBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        SolRegistries.MOD.apply(BlockHolder.class, (registry, id, it) -> {
            for (Map.Entry<BlockModel, BlockHolder> entry : it.getBlockSet().entrySet()) {
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

            if (it.hasDrop()) {
                if (it.getDropCount() == null)
                    add(it.get(), createSilkTouchOnlyTable(it.getDrop().get()));
                else
                    add(it.get(), createSingleItemTable(it.getDrop().get(), it.getDropCount()));
            }
        });
    }
}