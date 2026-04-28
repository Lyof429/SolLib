package net.lcc.sollib.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lcc.sollib.core.SolFabricCore;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesClientMixin {
    @Inject(method = "bootStrap", at = @At("TAIL"))
    private static void bootstrapSol(CallbackInfo ci) {
            SolFabricCore.registerClient();
        }
}
