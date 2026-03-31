package net.lcc.sollib.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lcc.sollib.api.common.ui.bossbar.SolBossBarRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
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
        SolBossBarRegistry.SolBossBarRenderer renderer = SolBossBarRegistry.getRenderer(bossEvent);

        if (renderer != null) renderer.render(guiGraphics, x, y, bossEvent);
        else original.call(instance, guiGraphics, x, y, bossEvent);
    }
}