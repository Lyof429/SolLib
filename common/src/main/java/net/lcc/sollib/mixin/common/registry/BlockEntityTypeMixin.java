package net.lcc.sollib.mixin.common.registry;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {
    @ModifyReturnValue(method = "isValid", at = @At("RETURN"))
    private boolean isValid(boolean original, BlockState state) {
        BlockEntityType<?> self = (BlockEntityType<?>) (Object) this;
        return original || ((BlockEntityType.SIGN == self || BlockEntityType.HANGING_SIGN == self) &&
                state.getBlock() instanceof SignBlock);
    }
}