package net.lcc.sollib.mixin.render.block;

import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.lcc.sollib.api.client.SolClientRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockRenderer.class, remap = false)
public abstract class BlockRendererMixin {
    @Shadow public abstract void renderModel(BlockRenderContext ctx, ChunkBuildBuffers buffers);

    @Inject(method = "renderModel", at = @At("HEAD"), remap = false, require = 0)
    private void handleSodiumBlockRender(BlockRenderContext ctx, ChunkBuildBuffers buffers, CallbackInfo ci) {
        SolClientRegistries.Render.BLOCK.apply(
                (p, s) -> {
                    BlockRenderContext proxyContext = new BlockRenderContext(ctx.world());
                    proxyContext.update(
                            p,
                            new BlockPos((int) ctx.origin().x(), (int) ctx.origin().y(), (int) ctx.origin().z()),
                            s,
                            Minecraft.getInstance().getBlockRenderer().getBlockModel(s),
                            ctx.seed()
                    );
                    this.renderModel(proxyContext, buffers);
                },
                ctx.world(),
                ctx.state(),
                ctx.pos(),
                ctx.seed()
        );
    }
}