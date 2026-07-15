package net.lcc.sollib.mixin.common.data;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lcc.sollib.SolTest;
import net.lcc.sollib.api.common.data.runtime.condition.LoadCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.io.InputStream;
import java.util.List;

@Mixin(FallbackResourceManager.class)
public class FallbackResourceManagerMixin {
    @Shadow @Final private PackType type;

    @WrapOperation(
            method = "getResource",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/PackResources;getResource(Lnet/minecraft/server/packs/PackType;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/server/packs/resources/IoSupplier;")
    )
    private IoSupplier<InputStream> getConditionedResource(PackResources instance, PackType packType,
                                                           ResourceLocation id, Operation<IoSupplier<InputStream>> original) {
        IoSupplier<InputStream> resource = original.call(instance, packType, id);
        return resource != null && LoadCondition.shouldLoad(id, instance, packType) ? resource : null;
    }

    @ModifyReturnValue(
            method = "getResourceStack",
            at = @At("RETURN")
    )
    private List<Resource> getConditionedResourceStack(List<Resource> original, ResourceLocation id) {
        //IoSupplier<InputStream> resource = original.call(instance, packType, id);
        //return SolTest.MOD.getLogger().info( resource != null && LoadCondition.shouldLoad(id, instance, packType) ? resource : null );
        return original.stream().filter(r -> LoadCondition.shouldLoad(id, r.source(), this.type)).toList();
    }
}
