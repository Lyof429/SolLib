package net.lcc.sollib.mixin.common.data;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.event.SEvents;
import net.lcc.sollib.mixin.access.SimpleReloadInstanceAccessor;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleReloadInstance;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(SimpleReloadInstance.class)
public class SimpleReloadInstanceMixin {
    @Inject(method = "create", at = @At("HEAD"))
    private static void headReload(ResourceManager manager, List<PreparableReloadListener> listeners, Executor backgroundExecutor,
                                       Executor gameExecutor, CompletableFuture<Unit> alsoWaitedFor, boolean profiled, CallbackInfoReturnable<ReloadInstance> cir) {
        Thread.dumpStack();
        SolLib.MOD.getLogger().info("CLIENT");
        SEvents.ON_PRELOAD.emit(manager);
    }

    @Inject(method = "create", at = @At("RETURN"))
    private static <S> void tailReload(ResourceManager manager, List<PreparableReloadListener> listeners, Executor backgroundExecutor,
                                   Executor gameExecutor, CompletableFuture<Unit> alsoWaitedFor, boolean profiled, CallbackInfoReturnable<ReloadInstance> cir) {
        SimpleReloadInstanceAccessor<S> r = (SimpleReloadInstanceAccessor<S>) cir.getReturnValue();
        r.setAllDone(r.getAllDone().whenComplete((a, b) -> SEvents.ON_RELOAD.emit(manager)));
    }
}
