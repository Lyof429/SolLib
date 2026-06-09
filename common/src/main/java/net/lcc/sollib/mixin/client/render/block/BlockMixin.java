package net.lcc.sollib.mixin.client.render.block;

import net.lcc.sollib.api.client.SolClientRegistries;
import net.lcc.sollib.api.client.render.block.IAddedRenderBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void initBlockRenderer(BlockBehaviour.Properties properties, CallbackInfo ci) {
        if (this instanceof IAddedRenderBlock renderer)
            SolClientRegistries.Render.BLOCK.register(
                    state -> renderer.shouldRender(state) && state.is((Block) (Object) this),
                    renderer
            );
    }
}
