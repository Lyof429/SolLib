package net.lcc.sollib.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.data.block.BlockModel;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

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
                    case CUBE -> generator.createTrivialCube(holder.get());
                    case PILLAR -> generator.woodProvider(holder.get()).log(holder.get());
                    case ROTATABLE -> generator.createRotatedVariantBlock(holder.get());
                    case CROSS -> generator.createCrossBlockWithDefaultItem(holder.get(), BlockModelGenerators.TintState.NOT_TINTED);
                    case CROSS_POTTED -> pottedCrossBlock(generator, holder.get());
                    case CROSS_DIRECTIONAL -> directionalCrossBlock(generator, holder.get());
                    case DOOR -> generator.createDoor(holder.get());
                    case TRAPDOOR -> generator.createTrapdoor(holder.get());
                    case FLUID -> generator.createNonTemplateModelBlock(holder.get());
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

    private void pottedCrossBlock(BlockModelGenerators generators, Block block) {
        Block plant = ((FlowerPotBlock) block).getContent();
        TextureMapping textureMapping = TextureMapping.plant(plant);
        ResourceLocation modelId = ModelTemplates.FLOWER_POT_CROSS.create(block, textureMapping, generators.modelOutput);
        generators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, modelId));
    }

    private void directionalCrossBlock(BlockModelGenerators generators, Block block) {
        ResourceLocation modelId = ModelTemplates.CROSS.create(block, TextureMapping.cross(block), generators.modelOutput);

        var variantMap = PropertyDispatch.property(BlockStateProperties.FACING)
                .select(Direction.UP, Variant.variant()
                        .with(VariantProperties.MODEL, modelId)
                        .with(VariantProperties.X_ROT, VariantProperties.Rotation.R0))
                .select(Direction.DOWN, Variant.variant()
                        .with(VariantProperties.MODEL, modelId)
                        .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                .select(Direction.NORTH, Variant.variant()
                        .with(VariantProperties.MODEL, modelId)
                        .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.EAST, Variant.variant()
                        .with(VariantProperties.MODEL, modelId)
                        .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .select(Direction.SOUTH, Variant.variant()
                        .with(VariantProperties.MODEL, modelId)
                        .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0))
                .select(Direction.WEST, Variant.variant()
                        .with(VariantProperties.MODEL, modelId)
                        .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));

        generators.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block).with(variantMap)
        );

        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(block.asItem()), TextureMapping.layer0(block), generators.modelOutput);
    }
}
