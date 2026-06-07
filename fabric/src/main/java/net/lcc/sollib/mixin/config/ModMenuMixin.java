package net.lcc.sollib.mixin.config;

import com.terraformersmc.modmenu.ModMenu;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.lcc.sollib.api.client.ui.config.SolConfigScreen;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ModMenu.class, remap = false)
public class ModMenuMixin {
    @Inject(method = "getConfigScreenFactory", at = @At("TAIL"), cancellable = true)
    private static void getSolConfigScreen(String modid, CallbackInfoReturnable<ConfigScreenFactory<?>> cir) {
        SolModContainer modContainer = SolRegistries.MOD.get(modid);
        if (modContainer != null && cir.getReturnValue() == null)
            cir.setReturnValue(parent -> new SolConfigScreen(modContainer, parent));
    }
}
