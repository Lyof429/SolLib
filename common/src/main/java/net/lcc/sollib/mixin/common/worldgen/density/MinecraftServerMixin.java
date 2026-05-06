package net.lcc.sollib.mixin.common.worldgen.density;

import net.lcc.sollib.api.common.worldgen.density.ProgressionDensityFunction;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "loadLevel", at = @At("HEAD"))
    private void loadDensityFunctions(CallbackInfo ci) {
        ProgressionDensityFunction.reloadAll((MinecraftServer) (Object) this);
    }

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void closeDensityFunctions(CallbackInfo ci) {
        ProgressionDensityFunction.reloadAll(null);
    }
}
