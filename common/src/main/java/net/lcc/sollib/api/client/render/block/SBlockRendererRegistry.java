package net.lcc.sollib.api.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class SBlockRendererRegistry {
    private final Map<Predicate<BlockState>, IBlockRenderer> INSTANCES = new LinkedHashMap<>();

    /**
     * Manages registration of block state renderers introduced in SolLib <br/>
     * Automatically called on Block subclasses that implement {@link IBlockRenderer}
     *
     * @param condition Filters the actual block state to be processed by {@link IBlockRenderer}
     * @param renderer  Management of how block state should be rendered
     * @since 1.0
     */
    public void register(Predicate<BlockState> condition, IBlockRenderer renderer) {
        INSTANCES.put(condition, renderer);
    }

    /**
     * Processes custom renderers registered by {@link #register(Predicate, IBlockRenderer)}
     *
     * @since 1.0
     */
    @ApiStatus.Internal
    public void apply(BlockRenderDispatcher instance, BlockState state, BlockPos pos, BlockAndTintGetter view, PoseStack poseStack, VertexConsumer vertexConsumer, RandomSource random) {
        if (state == null) return;

        for (Map.Entry<Predicate<BlockState>, IBlockRenderer> entry : INSTANCES.entrySet()) {
            if (!entry.getKey().test(state)) continue;

            poseStack.pushPose();
            entry.getValue().render((p, s) -> instance.renderBatched(s, p, view, poseStack, vertexConsumer, true, random),
                    state, pos, view, random, poseStack, vertexConsumer);
            poseStack.popPose();
        }
    }

    /**
     * Processes custom renderers registered by {@link #register(Predicate, IBlockRenderer)} in case Sodium is present
     *
     * @since 1.0
     */
    @ApiStatus.Internal
    public void apply(IBlockRenderer.ARenderer blockRenderer, BlockAndTintGetter view, BlockState state, BlockPos pos, long seed) {
        if (state == null) return;

        for (Map.Entry<Predicate<BlockState>, IBlockRenderer> entry : INSTANCES.entrySet()) {
            if (!entry.getKey().test(state)) continue;
            entry.getValue().render(blockRenderer, state, pos, view, RandomSource.create(seed), null, null);
        }
    }
}