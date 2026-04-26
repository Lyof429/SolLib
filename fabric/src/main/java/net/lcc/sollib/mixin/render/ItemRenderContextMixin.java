package net.lcc.sollib.mixin.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.ItemRenderContext;
import net.lcc.sollib.api.SolClientRegistries;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderContext.class)
public class ItemRenderContextMixin {
    @Inject(method = "renderModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/BakedModel;emitItemQuads(Lnet/minecraft/world/item/ItemStack;Ljava/util/function/Supplier;Lnet/fabricmc/fabric/api/renderer/v1/render/RenderContext;)V"))
    public void renderItem(ItemStack stack, ItemDisplayContext displayContext, boolean invert, PoseStack poseStack,
                           MultiBufferSource buffer, int packedLight, int packedOverlay, BakedModel model, CallbackInfo ci) {
        SolClientRegistries.ITEM_RENDERER.apply(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
    }
}
