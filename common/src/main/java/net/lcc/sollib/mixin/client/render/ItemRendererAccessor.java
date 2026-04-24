package net.lcc.sollib.mixin.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemRenderer.class)
public interface ItemRendererAccessor {
    @Invoker("renderModelLists")
    void onRenderModelLists(BakedModel model, ItemStack stack, int light, int overlay, PoseStack poseStack, VertexConsumer buffer);
}