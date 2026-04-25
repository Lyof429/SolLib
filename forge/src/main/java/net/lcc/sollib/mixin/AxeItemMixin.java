package net.lcc.sollib.mixin;

import net.lcc.sollib.event.AxeStrippableEvent;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    @Inject(method = "getAxeStrippingState", at = @At("RETURN"), cancellable = true, remap = false)
    private static void getSolStrippingState(BlockState original, CallbackInfoReturnable<BlockState> cir) {
        if (cir.getReturnValue() != null) return;

        AxeStrippableEvent event = new AxeStrippableEvent(original.getBlock());
        MinecraftForge.EVENT_BUS.post(event);

        if (event.hasStrippingResult())
            cir.setReturnValue(event.getStrippingResult().defaultBlockState().setValue(RotatedPillarBlock.AXIS, original.getValue(RotatedPillarBlock.AXIS)));
    }
}
