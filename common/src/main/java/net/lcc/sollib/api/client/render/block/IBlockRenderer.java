package net.lcc.sollib.api.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface IBlockRenderer {
    /**
     *
     * @param renderer A block renderer
     * @param poseStack Will be null if Sodium is present!
     * @param vertexConsumer Will be null if Sodium is present!
     */
    void render(BiConsumer<BlockPos, BlockState> renderer, BlockState state, BlockPos pos, BlockAndTintGetter getter,
                RandomSource random, @Nullable PoseStack poseStack, @Nullable VertexConsumer vertexConsumer);
}