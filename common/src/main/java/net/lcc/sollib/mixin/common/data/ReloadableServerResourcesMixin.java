package net.lcc.sollib.mixin.common.data;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.data.reload.SReloadRegistry;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.CompletableFuture;

@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin {
    @ModifyReturnValue(method = "loadResources", at = @At("RETURN"))
    private static CompletableFuture<ReloadableServerResources> returnLoadResources(CompletableFuture<ReloadableServerResources> original,
                                                                                    ResourceManager manager) {
        return original.whenComplete((a, b) -> SolRegistries.RELOADER.reload(manager));
    }
}
