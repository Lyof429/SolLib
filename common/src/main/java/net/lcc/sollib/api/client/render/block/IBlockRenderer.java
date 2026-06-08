package net.lcc.sollib.api.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface IBlockRenderer {
    /**
     * Renders a BlockState at the specified BlockPos <br/>
     * Implementation varies depending on Sodium presence, prefer using this over {@link BlockRenderDispatcher} or something else
     */
    @FunctionalInterface
    interface ARenderer {
        void renderBlock(BlockPos pos, BlockState state);
    }

    /**
     *
     * @param renderer An {@link ARenderer}, whose implementation depends on rendering mods present
     * @param poseStack Will be null if Sodium is present!
     * @param vertexConsumer Will be null if Sodium is present!
     */
    void render(ARenderer renderer, BlockState state, BlockPos pos, BlockAndTintGetter getter,
                RandomSource random, @Nullable PoseStack poseStack, @Nullable VertexConsumer vertexConsumer);
}