package net.lcc.sollib.mixin.common.data;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.lcc.sollib.api.common.SolRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

@Mixin(value = MultiPackResourceManager.class, priority = 100)
public class MultiPackResourceManagerMixin {
    // Using predefined lambdas so we don't have to allocate them each time
    @Unique private static final Predicate<Map.Entry<ResourceLocation, Resource>> sol_isNull
            = entry -> entry.getValue() == null;
    @Unique private static final BiFunction<ResourceLocation, List<Resource>, List<Resource>> sol_replaceList
            = (id, list) -> list.stream().map(resource -> SolRegistries.Data.RUNTIME.apply(id, resource)).filter(Objects::nonNull).toList();
    @Unique private static final Predicate<Map.Entry<ResourceLocation, List<Resource>>> sol_isEmpty
            = entry -> entry.getValue().isEmpty();

    @WrapMethod(method = "getResource")
    private Optional<Resource> getRuntimeResource(ResourceLocation id, Operation<Optional<Resource>> original) {
        return Optional.ofNullable(SolRegistries.Data.RUNTIME.apply(id, original.call(id).orElse(null)));
    }

    @WrapMethod(method = "getResourceStack")
    private List<Resource> getRuntimeResourceStack(ResourceLocation id, Operation<List<Resource>> original) {
        return original.call(id).stream().map(resource -> SolRegistries.Data.RUNTIME.apply(id, resource)).toList();
    }

    @WrapMethod(method = "listResources")
    private Map<ResourceLocation, Resource> listRuntimeResources(String startingPath, Predicate<ResourceLocation> allowedPathPredicate,
                                                                 Operation<Map<ResourceLocation, Resource>> original) {
        Map<ResourceLocation, Resource> result = original.call(startingPath, allowedPathPredicate);
        for (ResourceLocation id : SolRegistries.Data.RUNTIME.findMatching(startingPath, allowedPathPredicate))
            result.putIfAbsent(id, null);
        result.replaceAll(SolRegistries.Data.RUNTIME::apply);
        result.entrySet().removeIf(sol_isNull);
        return result;
    }

    @WrapMethod(method = "listResourceStacks")
    private Map<ResourceLocation, List<Resource>> listRuntimeResourceStacks(String startingPath, Predicate<ResourceLocation> allowedPathPredicate,
                                                                            Operation<Map<ResourceLocation, List<Resource>>> original) {
        Map<ResourceLocation, List<Resource>> result = original.call(startingPath, allowedPathPredicate);
        for (ResourceLocation id : SolRegistries.Data.RUNTIME.findMatching(startingPath, allowedPathPredicate))
            result.putIfAbsent(id, Collections.singletonList(null));
        result.replaceAll(sol_replaceList);
        result.entrySet().removeIf(sol_isEmpty);
        return result;
    }
}
