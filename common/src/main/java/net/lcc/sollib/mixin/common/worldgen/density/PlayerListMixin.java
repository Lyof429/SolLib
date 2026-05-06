package net.lcc.sollib.mixin.common.worldgen.density;

import net.lcc.sollib.api.common.worldgen.density.ProgressionDensityFunction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Shadow @Final private MinecraftServer server;

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void joinDensityFunctions(CallbackInfo ci) {
        ProgressionDensityFunction.reloadAll(this.server);
    }
}
