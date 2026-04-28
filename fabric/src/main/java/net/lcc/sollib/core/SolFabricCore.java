package net.lcc.sollib.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.EntityHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class SolFabricCore {
    public static void register() {
        SolRegistries.MOD.iterate(ItemHolder.class, holder -> Registry.register(BuiltInRegistries.ITEM, holder.getID(), holder.get()));
        SolRegistries.MOD.iterate(BlockHolder.class, holder -> Registry.register(BuiltInRegistries.BLOCK, holder.getID(), holder.get()));
        SolRegistries.MOD.iterate(EntityHolder.class, holder -> Registry.register(BuiltInRegistries.ENTITY_TYPE, holder.getID(), holder.get()));

        SolRegistries.MOD.iterate(ItemHolder.class, holder -> {
            if (holder.isFuel())
                FuelRegistry.INSTANCE.add(holder.get(), holder.getFuelDuration());
        });

        SolRegistries.MOD.iterate(BlockHolder.class, holder -> {
            if (holder.hasStripResult())
                StrippableBlockRegistry.register(holder.get(), holder.getStripResult().get());
        });
        SolRegistries.MOD.iterate(BlockHolder.class, holder -> {
            if (holder.isFlammable())
                FlammableBlockRegistry.getDefaultInstance().add(holder.get(), holder.getFlammability().ignite(), holder.getFlammability().spread());
        });
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        SolRegistries.MOD.iterate(BlockHolder.class, holder -> {
            if (holder.isCutout()) BlockRenderLayerMap.INSTANCE.putBlock(holder.get(), RenderType.cutout());
        });
    }
}
