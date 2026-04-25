package net.lcc.sollib.mixin;

import net.lcc.sollib.SolLibFabric;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BuiltInRegistries.class, priority = 800)
public class BuiltInRegistriesMixin {
    @Inject(method = "bootStrap", at = @At("HEAD"))
    private static void bootstrapSol(CallbackInfo ci) {
        SolLibFabric.register();
    }
}
