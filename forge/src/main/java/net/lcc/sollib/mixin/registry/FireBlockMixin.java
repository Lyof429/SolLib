package net.lcc.sollib.mixin.registry;

import net.lcc.sollib.event.SBlockFlammabilityEvent;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireBlock.class)
public class FireBlockMixin {
    @Inject(method = "getIgniteOdds(Lnet/minecraft/world/level/block/state/BlockState;)I", at = @At("RETURN"), cancellable = true)
    private void getSolIgniteChance(BlockState state, CallbackInfoReturnable<Integer> cir) {
        if (cir.getReturnValue() > 0 ||
                (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED))) {
            return;
        }

        SBlockFlammabilityEvent event = new SBlockFlammabilityEvent(state.getBlock());
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isFlammable())
            cir.setReturnValue(event.getFlammability().ignite());
    }

    @Inject(method = "getBurnOdds", at = @At("RETURN"), cancellable = true)
    private void getSolSpreadChance(BlockState state, CallbackInfoReturnable<Integer> cir) {
        if (cir.getReturnValue() > 0 ||
                (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED))) {
            return;
        }

        SBlockFlammabilityEvent event = new SBlockFlammabilityEvent(state.getBlock());
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isFlammable())
            cir.setReturnValue(event.getFlammability().spread());
    }
}
