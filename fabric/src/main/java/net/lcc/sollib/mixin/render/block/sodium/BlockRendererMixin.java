package net.lcc.sollib.mixin.render.block.sodium;

import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.lcc.sollib.api.client.SolClientRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockRenderer.class, remap = false)
public abstract class BlockRendererMixin {

    @Shadow
    public abstract void renderModel(BlockRenderContext ctx, ChunkBuildBuffers buffers);

    @Inject(method = "renderModel", at = @At("HEAD"), remap = false, require = 0)
    private void handleSodiumBlockRender(BlockRenderContext ctx, ChunkBuildBuffers buffers, CallbackInfo ci) {
        SolClientRegistries.Render.BLOCK.apply(
                (context, buildBuffers) -> this.renderModel((BlockRenderContext) context, (ChunkBuildBuffers) buildBuffers),
                ctx.world(),
                ctx.state(),
                ctx.pos(),
                ctx.origin(),
                buffers,
                ctx.seed()
        );
    }
}