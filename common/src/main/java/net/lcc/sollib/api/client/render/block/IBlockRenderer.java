package net.lcc.sollib.api.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface IBlockRenderer {
    void render(BlockRenderDispatcher instance, BlockState state, BlockPos pos, BlockAndTintGetter getter, PoseStack poseStack, VertexConsumer vertexConsumer, RandomSource random);
}