package net.lcc.sollib.mixin.common.worldgen.density;

import net.lcc.sollib.api.common.worldgen.density.ProgressionDensityFunction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {
    @Shadow @Final private PlayerList playerList;

    @Inject(method = "markForVisibilityUpdate", at = @At("TAIL"))
    private void updateDensityFunctions(CallbackInfo ci) {
        ProgressionDensityFunction.reloadAll(this.playerList.getServer());
    }
}
