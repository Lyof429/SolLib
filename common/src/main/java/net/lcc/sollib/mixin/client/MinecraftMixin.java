package net.lcc.sollib.mixin.client;

import net.lcc.sollib.api.client.SolClientRegistries;
import net.lcc.sollib.mixin.access.BossHealthOverlayAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.sounds.Music;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    @Final
    public Gui gui;

    @Inject(method = "getSituationalMusic", at = @At("HEAD"), cancellable = true)
    private void getSituationalMusic(CallbackInfoReturnable<Music> cir) {
        BossHealthOverlayAccessor accessor = (BossHealthOverlayAccessor) this.gui.getBossOverlay();
        if (!accessor.getEvents().isEmpty()) {
            Music music = SolClientRegistries.BOSS_BAR.getCustomBossMusic(accessor);
            if (music != null) {
                cir.setReturnValue(music);
            }
        }
    }
}