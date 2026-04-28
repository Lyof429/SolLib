package net.lcc.sollib.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.EntityHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.function.BiConsumer;

public class SEntityLootTableProvider extends SimpleFabricLootTableProvider {
    public SEntityLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput, LootContextParamSets.ENTITY);
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output) {
        SolRegistries.MOD.iterate(EntityHolder.class, holder -> {
            if (holder.hasDrop())
                output.accept(holder.getID(), holder.getDrop().get());
        });
    }
}