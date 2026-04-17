package net.lcc.sollib.mixin.common.data;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lcc.sollib.api.SolRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

@Mixin(MultiPackResourceManager.class)
public class MultiPackResourceManagerMixin {
    /**
     * Config reloading and reload listener preloading
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    private void tailInit(PackType type, List<PackResources> packs, CallbackInfo ci) {
        SolRegistries.CONFIG.reload();
        SolRegistries.RELOADER.preload((ResourceManager) (Object) this);
    }


    // Using predefined lambdas so we don't have to allocate them each time
    @Unique private static final Predicate<Map.Entry<ResourceLocation, Resource>> sol_isNull
            = entry -> entry.getValue() == null;
    @Unique private static final BiFunction<ResourceLocation, List<Resource>, List<Resource>> sol_replaceList
            = (id, list) -> list.stream().map(resource -> SolRegistries.RUNTIME.apply(id, resource)).toList();
    @Unique private static final Predicate<Map.Entry<ResourceLocation, List<Resource>>> sol_isEmpty
            = entry -> entry.getValue().isEmpty();

    @ModifyReturnValue(method = "getResource", at = @At("RETURN"))
    private Optional<Resource> getRuntimeResource(Optional<Resource> original, ResourceLocation id) {
        return Optional.ofNullable(SolRegistries.RUNTIME.apply(id, original.orElse(null)));
    }

    @ModifyReturnValue(method = "getResourceStack", at = @At("RETURN"))
    private List<Resource> getRuntimeResourceStack(List<Resource> original, ResourceLocation id) {
        return original.stream().map(resource -> SolRegistries.RUNTIME.apply(id, resource)).toList();
    }

    @ModifyReturnValue(method = "listResources", at = @At("RETURN"))
    private Map<ResourceLocation, Resource> listRuntimeResources(Map<ResourceLocation, Resource> original,
                                                                    String startingPath, Predicate<ResourceLocation> allowedPathPredicate) {
        for (ResourceLocation id : SolRegistries.RUNTIME.findMatching(startingPath, allowedPathPredicate))
            original.putIfAbsent(id, null);
        original.replaceAll(SolRegistries.RUNTIME::apply);
        original.entrySet().removeIf(sol_isNull);
        return original;
    }

    @ModifyReturnValue(method = "listResourceStacks", at = @At("RETURN"))
    private Map<ResourceLocation, List<Resource>> listRuntimeResourceStacks(Map<ResourceLocation, List<Resource>> original,
                                                                          String startingPath, Predicate<ResourceLocation> allowedPathPredicate) {
        for (ResourceLocation id : SolRegistries.RUNTIME.findMatching(startingPath, allowedPathPredicate))
            original.putIfAbsent(id, List.of());
        original.replaceAll(sol_replaceList);
        original.entrySet().removeIf(sol_isEmpty);
        return original;
    }
}
