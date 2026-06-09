package net.lcc.sollib.mixin.client.render.item;

import net.lcc.sollib.api.client.render.item.IAddedBarItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Shadow public abstract void fill(RenderType renderType, int minX, int minY, int maxX, int maxY, int color);

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = At.Shift.BEFORE))
    public void renderAddedBar(Font textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        if (stack.getItem() instanceof IAddedBarItem barAdder) {
            int i = Math.round(barAdder.getAddedBarFullness(stack) * 13);
            int j = barAdder.getAddedBarColor(stack);
            this.fill(RenderType.guiOverlay(), x + 2, y + 14, x + 15, y + 15, -16777216);
            this.fill(RenderType.guiOverlay(), x + 2, y + 14, x + 2 + i, y + 15, j | -16777216);
        }
    }
}
