package net.lcc.sollib.mixin.client.render;

import net.lcc.sollib.api.SolClientRegistries;
import net.lcc.sollib.api.client.render.item.IItemRenderer;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void initItemRenderer(Item.Properties properties, CallbackInfo ci) {
        if (this instanceof IItemRenderer renderer)
            SolClientRegistries.ITEM_RENDERER.register(stack -> stack.is((Item) (Object) this), renderer);
    }
}
