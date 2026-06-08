package net.lcc.sollib.mixin.render.block;

import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.lcc.sollib.api.client.SolClientRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderer.class)
public abstract class BlockRendererMixin {
    @Shadow
    public abstract void renderModel(BakedModel model, BlockState state, BlockPos pos, BlockPos origin);

    @Inject(method = "renderModel", at = @At("HEAD"), remap = false, require = 0)
    private void handleSodiumBlockRender(BakedModel model, BlockState state, BlockPos pos, BlockPos origin, CallbackInfo ci) {
        SolClientRegistries.Render.BLOCK.apply(
                (p, s) -> this.renderModel(Minecraft.getInstance().getBlockRenderer().getBlockModel(s), s, p, pos),
                state,
                pos,
                pos.asLong()
        );
    }
}