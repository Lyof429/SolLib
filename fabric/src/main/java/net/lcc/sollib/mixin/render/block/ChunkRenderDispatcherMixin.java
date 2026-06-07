package net.lcc.sollib.mixin.render.block;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net.minecraft.client.renderer.chunk.ChunkRenderDispatcher$RenderChunk$RebuildTask")
public class ChunkRenderDispatcherMixin {
    // TODO: Sasha that doesn't exist anymore idk how to fix it
    /*
    @WrapOperation(
            method = "compile",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;renderBatched(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/BlockAndTintGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;)V"
            )
    )
    private void handleBlockRender(
            BlockRenderDispatcher instance, BlockState state, BlockPos pos, BlockAndTintGetter level, PoseStack poseStack, VertexConsumer consumer, boolean checkSides, RandomSource random, Operation<Void> original
    ) {
        original.call(instance, state, pos, level, poseStack, consumer, checkSides, random);
        SolClientRegistries.Render.BLOCK.apply(
                instance,
                state,
                pos,
                level,
                poseStack,
                consumer,
                random
        );
    }*/
}