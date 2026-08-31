package net.lcc.sollib.datagen;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.data.block.BlockModel;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class SBlockStateProvider extends BlockStateProvider {
    public SBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, SolLib.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        SolRegistries.MOD.iterate(BlockHolder.class, holder -> {
            if (holder.getBlockSet().isEmpty() && holder.hasModel()) {
                Block block = holder.get();
                switch (holder.getModel()) {
                    case CUBE -> simpleBlock(block);
                    case PILLAR -> logBlock((RotatedPillarBlock) block);
                    case ROTATABLE -> directionalBlock(block, models().cubeAll(name(block), blockTexture(block)));
                    case CROSS ->
                            simpleBlock(block, models().cross(name(block), blockTexture(block)).renderType("cutout"));
                    case CROSS_POTTED -> pottedCrossBlock(block);
                    case CROSS_DIRECTIONAL -> directionalCrossBlock(block);
                    case DOOR ->
                            doorBlockWithRenderType((DoorBlock) block, modLoc("block/" + name(block) + "_bottom"), modLoc("block/" + name(block) + "_top"), "cutout");
                    case TRAPDOOR ->
                            trapdoorBlockWithRenderType((TrapDoorBlock) block, blockTexture(block), true, "cutout");
                    case FLUID ->
                            simpleBlock(block, models().getBuilder(name(block)).texture("particle", blockTexture(block)));
                }
            } else {
                Block baseBlock = holder.get();
                ResourceLocation texture = blockTexture(baseBlock);
                for (Map.Entry<BlockModel, BlockHolder> entry : holder.getBlockSet().entrySet()) {
                    Block childBlock = entry.getValue().get();
                    switch (entry.getKey()) {
                        case STAIRS -> stairsBlock((StairBlock) childBlock, texture);
                        case SLAB ->
                                slabBlock((SlabBlock) childBlock, ForgeRegistries.BLOCKS.getKey(baseBlock), texture, texture, texture);
                        case WALL -> wallBlock((WallBlock) childBlock, texture);
                        case PRESSURE_PLATE -> pressurePlateBlock((PressurePlateBlock) childBlock, texture);
                        case BUTTON -> buttonBlock((ButtonBlock) childBlock, texture);
                        case FENCE -> fenceBlock((FenceBlock) childBlock, texture);
                        case FENCE_GATE -> fenceGateBlock((FenceGateBlock) childBlock, texture);
                    }
                }
            }
        });
    }

    private void pottedCrossBlock(Block block) {
        Block plant = ((FlowerPotBlock) block).getContent();
        ModelFile model = models().withExistingParent(name(block), mcLoc("block/flower_pot_cross"))
                .texture("plant", blockTexture(plant))
                .renderType("cutout");
        simpleBlock(block, model);
    }

    private void directionalCrossBlock(Block block) {
        ModelFile modelId = models().cross(name(block), blockTexture(block)).renderType("cutout");

        getVariantBuilder(block).forAllStates(state -> {
            Direction dir = state.getValue(BlockStateProperties.FACING);
            int x = 0;
            int y = 0;
            switch (dir) {
                case UP -> {
                    x = 0;
                    y = 0;
                }
                case DOWN -> {
                    x = 180;
                    y = 0;
                }
                case NORTH -> {
                    x = 90;
                    y = 180;
                }
                case EAST -> {
                    x = 90;
                    y = 270;
                }
                case SOUTH -> {
                    x = 90;
                    y = 0;
                }
                case WEST -> {
                    x = 90;
                    y = 90;
                }
            }
            return ConfiguredModel.builder()
                    .modelFile(modelId)
                    .rotationX(x)
                    .rotationY(y)
                    .build();
        });
    }

    @Override
    public void pressurePlateBlock(PressurePlateBlock block, ResourceLocation texture) {
        ModelFile model = models().pressurePlate(name(block), texture);
        ModelFile modelDown = models().pressurePlateDown(name(block) + "_down", texture);

        getVariantBuilder(block).forAllStates(state -> {
            boolean powered = state.getValue(BlockStateProperties.POWERED);
            return ConfiguredModel.builder().modelFile(powered ? modelDown : model).build();
        });
    }

    @Override
    public void buttonBlock(ButtonBlock block, ResourceLocation texture) {
        ModelFile model = models().button(name(block), texture);
        ModelFile modelPressed = models().buttonPressed(name(block) + "_pressed", texture);

        getVariantBuilder(block).forAllStates(state -> {
            boolean powered = state.getValue(BlockStateProperties.POWERED);
            AttachFace face = state.getValue(BlockStateProperties.ATTACH_FACE);
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            int x = 0;
            int y = 0;

            switch (face) {
                case FLOOR -> {
                    x = 0;
                    switch (facing) {
                        case NORTH -> y = 0;
                        case EAST -> y = 90;
                        case SOUTH -> y = 180;
                        case WEST -> y = 270;
                    }
                }
                case WALL -> {
                    x = 90;
                    switch (facing) {
                        case NORTH -> y = 0;
                        case EAST -> y = 90;
                        case SOUTH -> y = 180;
                        case WEST -> y = 270;
                    }
                }
                case CEILING -> {
                    x = 180;
                    switch (facing) {
                        case NORTH -> y = 180;
                        case EAST -> y = 270;
                        case SOUTH -> y = 0;
                        case WEST -> y = 90;
                    }
                }
            }
            return ConfiguredModel.builder()
                    .modelFile(powered ? modelPressed : model)
                    .rotationX(x)
                    .rotationY(y)
                    .build();
        });
    }

    private String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }
}