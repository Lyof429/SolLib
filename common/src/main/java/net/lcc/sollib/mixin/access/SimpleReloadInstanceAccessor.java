package net.lcc.sollib.mixin.access;

import net.minecraft.server.packs.resources.SimpleReloadInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mixin(SimpleReloadInstance.class)
public interface SimpleReloadInstanceAccessor<S> {
    @Accessor CompletableFuture<List<S>> getAllDone();
    @Accessor void setAllDone(CompletableFuture<List<S>> allDone);
}
