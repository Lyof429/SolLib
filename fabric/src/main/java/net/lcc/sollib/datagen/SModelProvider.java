package net.lcc.sollib.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.data.BlockModel;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;

import java.util.Map;

public class SModelProvider extends FabricModelProvider {
    public SModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        SolRegistries.MOD.iterate(BlockHolder.class, holder -> {
            if (holder.getBlockSet().isEmpty() && holder.hasModel()) {
                switch (holder.getModel()) {
                    case CUBE -> {
                        generator.createTrivialCube(holder.get());
                    }
                    case PILLAR -> {
                        generator.woodProvider(holder.get()).log(holder.get());
                    }
                    case ROTATABLE -> generator.createRotatedVariantBlock(holder.get());
                    case CROSS -> generator.createCrossBlockWithDefaultItem(holder.get(), BlockModelGenerators.TintState.NOT_TINTED);
                    case DOOR -> generator.createDoor(holder.get());
                    case TRAPDOOR -> generator.createTrapdoor(holder.get());
                }
            } else {
                BlockModelGenerators.BlockFamilyProvider familyProvider = generator.family(holder.get());
                for (Map.Entry<BlockModel, BlockHolder> entry : holder.getBlockSet().entrySet()) {
                    switch (entry.getKey()) {
                        case STAIRS -> familyProvider.stairs(entry.getValue().get());
                        case SLAB -> familyProvider.slab(entry.getValue().get());
                        case WALL -> familyProvider.wall(entry.getValue().get());
                        case PRESSURE_PLATE -> familyProvider.pressurePlate(entry.getValue().get());
                        case BUTTON -> familyProvider.button(entry.getValue().get());
                        case FENCE -> familyProvider.fence(entry.getValue().get());
                        case FENCE_GATE -> familyProvider.fenceGate(entry.getValue().get());
                    }
                }
            }
        });
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        SolRegistries.MOD.iterate(ItemHolder.class, holder -> {
            if (holder.hasModel())
                generator.generateFlatItem(holder.get(), holder.getModel());
        });
    }
}
