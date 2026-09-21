package net.lcc.sollib.mixin.common.data;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.event.SEvents;
import net.minecraft.client.ResourceLoadStateTracker;
import net.minecraft.commands.Commands;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin {
    @ModifyReturnValue(method = "loadResources", at = @At("RETURN"))
    private static CompletableFuture<ReloadableServerResources>
    wrapReload(CompletableFuture<ReloadableServerResources> original, ResourceManager manager,
                        LayeredRegistryAccess<RegistryLayer> registries, FeatureFlagSet enabledFeatures,
                        Commands.CommandSelection commandSelection, int functionCompilationLevel,
                        Executor backgroundExecutor, Executor gameExecutor) {

        return CompletableFuture.runAsync(() -> {
                    SEvents.ON_PRELOAD.emit(manager);
                }, gameExecutor)
                .thenCompose(a -> original)
                .whenComplete((a, b) -> {
                    SEvents.ON_RELOAD.emit(manager);
                });
    }
}
