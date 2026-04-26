package net.lcc.sollib.mixin.config;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.client.ui.config.SolConfigScreen;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.forgespi.language.IModInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.BiFunction;

@Mixin(value = ConfigScreenHandler.class, remap = false)
public class ConfigScreenHandlerMixin {
    @Inject(method = "getScreenFactoryFor", at = @At("TAIL"), cancellable = true)
    private static void getSolConfigScreen(IModInfo selected, CallbackInfoReturnable<Optional<BiFunction<Minecraft, Screen, Screen>>> cir) {
        SolModContainer modContainer = SolRegistries.MOD.get(selected.getModId());
        if (modContainer != null && cir.getReturnValue().isEmpty())
            cir.setReturnValue(Optional.of((minecraft, screen) -> new SolConfigScreen(modContainer, screen)));
    }
}
