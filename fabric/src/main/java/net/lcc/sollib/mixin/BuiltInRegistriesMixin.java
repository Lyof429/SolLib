package net.lcc.sollib.mixin;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.SolTest;
import net.lcc.sollib.api.common.registry.ItemHolder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BuiltInRegistries.class, priority = 800)
public class BuiltInRegistriesMixin {
    @Inject(method = "bootStrap", at = @At("HEAD"))
    private static void bootstrapSol(CallbackInfo ci) {
        SolLib.LOG.info("Hooked into registries!");
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.tryBuild("sollib", "test"),
                SolTest.MOD.getRegistrar(ItemHolder.class).get("test").get());
    }
}
