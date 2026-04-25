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
        SolRegistries.MOD.apply(BlockHolder.class, (registry, id, it) -> {
            if (it.getBlockSet().isEmpty() && it.hasModel()) {
                switch (it.getModel()) {
                    case CUBE -> {
                        generator.createTrivialCube(it.get());
                    }
                    case PILLAR -> {
                        generator.woodProvider(it.get()).log(it.get());
                    }
                    case ROTATABLE -> generator.createRotatedVariantBlock(it.get());
                    case CROSS -> generator.createCrossBlockWithDefaultItem(it.get(), BlockModelGenerators.TintState.NOT_TINTED);
                    case DOOR -> generator.createDoor(it.get());
                    case TRAPDOOR -> generator.createTrapdoor(it.get());
                }
            } else {
                BlockModelGenerators.BlockFamilyProvider familyProvider = generator.family(it.get());
                for (Map.Entry<BlockModel, BlockHolder> entry : it.getBlockSet().entrySet()) {
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
        SolRegistries.MOD.apply(ItemHolder.class, (registry, id, it) -> {
            if (it.hasModel())
                generator.generateFlatItem(it.get(), it.getModel());
        });
    }
}
