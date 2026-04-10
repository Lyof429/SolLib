package net.lcc.sollib.mixin.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lcc.sollib.api.client.render.item.SolBuiltinItemRenderRegistry;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin {
    @Shadow
    @Final
    private EntityModelSet entityModelSet;

    @Inject(method = "renderByItem", at = @At("TAIL"))
    public void renderItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, CallbackInfo ci) {
        SolBuiltinItemRenderRegistry.SolItemRenderer renderer = SolBuiltinItemRenderRegistry.getRenderer(stack);
        if (renderer != null) {
            poseStack.pushPose();
            renderer.render(stack, displayContext, poseStack, buffer, packedLight, packedOverlay, entityModelSet);
            poseStack.popPose();
        }
    }
}