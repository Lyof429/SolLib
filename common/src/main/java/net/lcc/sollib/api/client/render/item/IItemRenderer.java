package net.lcc.sollib.api.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface IItemRenderer {
    void render(ItemStack stack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer,
                int pPackedLight, int pPackedOverlay);
}