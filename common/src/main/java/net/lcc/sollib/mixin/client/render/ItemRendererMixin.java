package net.lcc.sollib.mixin.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lcc.sollib.api.SolClientRegistries;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V"))
    public void renderItem(ItemStack stack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack,
                           MultiBufferSource buffer, int packedLight, int packedOverlay, BakedModel model, CallbackInfo ci) {
        SolClientRegistries.ITEM_RENDERER.apply(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
    }
}