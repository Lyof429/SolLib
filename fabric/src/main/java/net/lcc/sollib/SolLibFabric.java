package net.lcc.sollib;

import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.lcc.sollib.api.client.SolClientRegistries;
import net.lcc.sollib.api.common.data.runtime.condition.LoadCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SolLibFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        SolLib.init();
        SolClientRegistries.Render.BLOCK.registerSodium(state -> state.is(Blocks.BAMBOO) && state.getValue(BlockStateProperties.BAMBOO_LEAVES).equals(BambooLeaves.LARGE),
                (render, getter, state, pos, origin, buffers, seed) -> {
                    BlockState azaleaState = Blocks.AZALEA.defaultBlockState();
                    WorldSlice level = (WorldSlice) getter;
                    BlockRenderContext proxyContext = new BlockRenderContext(level);
                    proxyContext.update(
                            pos,
                            new BlockPos((int) origin.x(), (int) origin.y(), (int) origin.z()),
                            azaleaState,
                            Minecraft.getInstance().getBlockRenderer().getBlockModel(azaleaState),
                            seed
                    );
                    render.accept(proxyContext, buffers);
                });
        ResourceConditions.register(LoadCondition.CONFIG, LoadCondition::configMatches);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
    }
}
