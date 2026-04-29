package net.lcc.sollib.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.EntityHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.lcc.sollib.platform.Services;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.*;

import java.util.Map;
import java.util.function.Supplier;

public class SolFabricCore {
    public static void register() {
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

        SolRegistries.MOD.iterate(EntityHolder.class, holder -> {
            if (holder.hasAttributes())
                FabricDefaultAttributeRegistry.register((EntityType<? extends LivingEntity>) holder.get(), holder.getAttributes());
        });
        SolRegistries.MOD.iterate(EntityHolder.class, holder -> {
            if (holder.hasSpawnRestrictions())
                SpawnPlacements.register((EntityType<Mob>) holder.get(), holder.getSpawnRestrictions().location(),
                        holder.getSpawnRestrictions().heightmap(), holder.getSpawnRestrictions().predicate());
        });
        SolRegistries.MOD.iterate(EntityHolder.class, holder -> {
            if (holder.shouldSpawn())
                BiomeModifications.addSpawn(context ->
                        holder.getSpawn().matchesBiome(key -> context.getBiomeKey().equals(key), context::hasTag),
                        holder.getSpawn().category(), holder.get(),
                        holder.getSpawn().weight(), holder.getSpawn().min(), holder.getSpawn().max());
        });
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        SolRegistries.MOD.iterate(BlockHolder.class, holder -> {
            if (holder.isCutout()) BlockRenderLayerMap.INSTANCE.putBlock(holder.get(), RenderType.cutout());
        });

        SolRegistries.MOD.iterate(EntityHolder.class, holder -> {
            if (holder.hasRenderer())
                EntityRendererRegistry.register(holder.get(), holder.getRenderer());
        });

        SolRegistries.MOD.iterate(EntityHolder.class, holder -> {
            for (Map.Entry<ModelLayerLocation, Supplier<LayerDefinition>> entry : holder.getModelLayers())
                EntityModelLayerRegistry.registerModelLayer(entry.getKey(), () -> entry.getValue().get());
        });
    }
}
