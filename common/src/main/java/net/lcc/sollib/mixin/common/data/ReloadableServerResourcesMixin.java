package net.lcc.sollib.mixin.common.data;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.event.SEvents;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.CompletableFuture;

@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin {
    @ModifyReturnValue(method = "loadResources", at = @At("RETURN"))
    private static CompletableFuture<ReloadableServerResources> returnLoadResources(CompletableFuture<ReloadableServerResources> original,
                                                                                    ResourceManager manager) {
        /*return CompletableFuture.completedFuture(Unit.INSTANCE)
                .whenComplete((a, b) -> {
                    SolLib.MOD.getLogger().info("preload");
                    SEvents.ON_PRELOAD.emit(manager);
                })
                .thenApplyAsync(a -> original.join())
                .whenComplete((a, b) -> {
                    SolLib.MOD.getLogger().info("reload");
                    SEvents.ON_RELOAD.emit(manager);
                });*/
        return original.whenComplete((a, b) -> SEvents.ON_RELOAD.emit(manager));
    }
}
