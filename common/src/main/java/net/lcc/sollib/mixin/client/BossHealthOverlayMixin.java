package net.lcc.sollib.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.lcc.sollib.api.client.SolClientRegistries;
import net.lcc.sollib.api.client.ui.bossbar.BossBarTextState;
import net.lcc.sollib.api.client.ui.bossbar.IBossBarRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BossHealthOverlay.class)
public class BossHealthOverlayMixin {
    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/BossHealthOverlay;drawBar(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/BossEvent;)V"
            )
    )
    private void wrapDrawBar(BossHealthOverlay instance, GuiGraphics guiGraphics, int x, int y, BossEvent bossEvent, Operation<Void> original) {
        IBossBarRenderer renderer = SolClientRegistries.BOSS_BAR.getRenderer(bossEvent);

        if (renderer != null) renderer.render(guiGraphics, x, y, bossEvent);
        else original.call(instance, guiGraphics, x, y, bossEvent);
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I"
            )
    )
    private int wrapDrawString(
            GuiGraphics instance,
            Font font,
            Component text,
            int x,
            int y,
            int color,
            Operation<Integer> original,
            @Local LerpingBossEvent event,
            @Local GuiGraphics guiGraphics
    ) {
        BossBarTextState state = new BossBarTextState(font, text, guiGraphics, x, y, color, true, 1.0f);
        BossBarTextState modified = SolClientRegistries.BOSS_BAR.getModifiedTextState(event, state, guiGraphics);

        instance.pose().pushPose();

        if (modified.scale() != 1.0f) {
            float textWidth = modified.font().width(modified.text());
            float centerX = modified.x() + (textWidth / 2.0f);
            float centerY = modified.y() + (modified.font().lineHeight / 2.0f);

            instance.pose().translate(centerX, centerY, 0.0f);
            instance.pose().scale(modified.scale(), modified.scale(), 1.0f);
            instance.pose().translate(-centerX, -centerY, 0.0f);
        }

        int result = instance.drawString(
                modified.font(),
                modified.text(),
                modified.x(),
                modified.y(),
                modified.color(),
                modified.dropShadow()
        );

        instance.pose().popPose();

        return result;
    }
}