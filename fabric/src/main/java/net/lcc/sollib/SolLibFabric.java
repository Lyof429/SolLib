package net.lcc.sollib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.lcc.sollib.api.client.SolClientRegistries;
import net.lcc.sollib.api.common.data.runtime.condition.LoadCondition;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SolLibFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        SolLib.init();

        SolClientRegistries.Render.BLOCK.register(state -> state.is(Blocks.BAMBOO) && state.getValue(BlockStateProperties.BAMBOO_LEAVES).equals(BambooLeaves.LARGE),
                (renderer, state, pos, getter, poseStack, vertexConsumer, random) -> {
            BlockState azaleaState = Blocks.AZALEA.defaultBlockState();
            renderer.renderBlock(pos, azaleaState);
        });

        ResourceConditions.register(LoadCondition.CONFIG, LoadCondition::configMatches);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
    }
}
