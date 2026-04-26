package net.lcc.sollib.mixin.config;

import com.terraformersmc.modmenu.ModMenu;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.client.ui.config.SolConfigScreen;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModMenu.class)
public class ModMenuMixin {
    @Inject(method = "getConfigScreen", at = @At("TAIL"), cancellable = true)
    private static void getSolConfigScreen(String modid, Screen menuScreen, CallbackInfoReturnable<Screen> cir) {
        SolModContainer modContainer = SolRegistries.MOD.get(modid);
        if (modContainer != null && cir.getReturnValue() == null)
            cir.setReturnValue(new SolConfigScreen(modContainer, menuScreen));
    }
}
