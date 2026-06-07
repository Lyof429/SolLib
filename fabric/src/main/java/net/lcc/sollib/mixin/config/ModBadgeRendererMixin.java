package net.lcc.sollib.mixin.config;

import com.terraformersmc.modmenu.util.mod.Mod;
import com.terraformersmc.modmenu.util.mod.ModBadgeRenderer;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModBadgeRenderer.class, remap = false)
public abstract class ModBadgeRendererMixin {
    @Shadow protected Mod mod;
    @Shadow public abstract void drawBadge(GuiGraphics DrawContext, FormattedCharSequence text, int outlineColor, int fillColor, int mouseX, int mouseY);

    @Unique private static final Component sol_badge =
            Component.translatable("gui.sollib.badge").withStyle(ChatFormatting.DARK_GRAY);

    @Inject(method = "draw", at = @At(value = "INVOKE", target = "Lcom/terraformersmc/modmenu/util/mod/Mod;getBadges()Ljava/util/Set;"))
    private void drawSolBadge(GuiGraphics drawContext, int mouseX, int mouseY, CallbackInfo ci) {
        SolModContainer modContainer = SolRegistries.MOD.get(this.mod.getId());
        if (modContainer != null)
            this.drawBadge(drawContext, sol_badge.getVisualOrderText(), -0x002AB5, -0x000026, mouseX, mouseY);
    }
}
