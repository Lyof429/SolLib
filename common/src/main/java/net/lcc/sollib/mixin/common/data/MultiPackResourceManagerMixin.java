package net.lcc.sollib.mixin.common.data;

import net.lcc.sollib.api.common.config.SolConfigRegistry;
import net.lcc.sollib.api.common.data.reload.SolReloadRegistry;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MultiPackResourceManager.class)
public class MultiPackResourceManagerMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void tailInit(PackType type, List<PackResources> packs, CallbackInfo ci) {
        //SolReloadRegistry.preload((ResourceManager) (Object) this);
        SolConfigRegistry.reload();
        SolReloadRegistry.preload((ResourceManager) (Object) this);
    }
}
