package net.lcc.sollib.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;

public class SolFabricCore {
    public static void register() {
        SolRegistries.MOD.apply(ItemHolder.class, (registry, id, instance) -> Registry.register(registry, id, instance.get()));
        SolRegistries.MOD.apply(BlockHolder.class, (registry, id, instance) -> Registry.register(registry, id, instance.get()));

        SolRegistries.MOD.apply(ItemHolder.class, (registry, id, instance) -> {
            if (instance.isFuel())
                FuelRegistry.INSTANCE.add(instance.get(), instance.getFuelDuration());
        });

        SolRegistries.MOD.apply(BlockHolder.class, (registry, id, instance) -> {
            if (instance.hasStripResult())
                StrippableBlockRegistry.register(instance.get(), instance.getStripResult().get());
        });
        SolRegistries.MOD.apply(BlockHolder.class, (registry, id, instance) -> {
            if (instance.isFlammable())
                FlammableBlockRegistry.getDefaultInstance().add(instance.get(), instance.getFlammability().ignite(), instance.getFlammability().spread());
        });
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        SolRegistries.MOD.apply(BlockHolder.class, (registry, id, instance) -> {
            if (instance.isCutout()) BlockRenderLayerMap.INSTANCE.putBlock(instance.get(), RenderType.cutout());
        });
    }
}
