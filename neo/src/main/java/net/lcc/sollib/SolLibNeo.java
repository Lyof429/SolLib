package net.lcc.sollib;

import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.EffectHolder;
import net.lcc.sollib.api.common.registry.holder.EntityHolder;
import net.lcc.sollib.core.Identifier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Map;
import java.util.function.Supplier;

@Mod(SolLib.MOD_ID)
@EventBusSubscriber(modid = SolLib.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class SolLibNeo {
    public SolLibNeo() {
        SolLib.init();
    }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        SolRegistries.MOD.register((registry, holder) -> event.register(registry, holder.getID(), holder));
        SolRegistries.MOD.iterate(EffectHolder.class, holder -> holder.registerPotion(
                potion -> event.register(Registries.POTION, potion.getID(), potion)
        ));

        SolRegistries.MOD.iterate(EntityHolder.class, holder -> {
            if (holder.shouldSpawn()) {
                ResourceLocation id = holder.getID();

                SolRegistries.Data.RUNTIME.addJson(Identifier.of(id.getNamespace(),
                                "tags/worldgen/biome/" + id.getPath() + "_can_spawn.json"),
                        json -> holder.getSpawn().createTag(json, id));
                SolRegistries.Data.RUNTIME.addJson(Identifier.of(id.getNamespace(),
                                "forge/biome_modifier/" + id.getPath() + ".json"),
                        json -> holder.getSpawn().createBiomeModifier(json, id));
            }
        });
    }

    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        SolRegistries.MOD.iterate(EntityHolder.class, holder -> {
            if (holder.hasRenderer())
                event.registerEntityRenderer(holder.get(), holder.getRenderer());
        });
    }

    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterLayerDefinitions event) {
        SolRegistries.MOD.iterate(EntityHolder.class, holder -> {
            for (Map.Entry<ModelLayerLocation, Supplier<LayerDefinition>> entry : holder.getModelLayers())
                event.registerLayerDefinition(entry.getKey(), entry.getValue());
        });
    }

    @SubscribeEvent
    public static void register(EntityAttributeCreationEvent event) {
        SolRegistries.MOD.iterate(EntityHolder.class, holder -> {
            if (holder.hasAttributes()) {
                // Forge is picky and wants its own attributes, else it explodes
                AttributeSupplier.Builder builder = holder.getAttributes();
                builder.add(NeoForgeMod.SWIM_SPEED)
                        .add(NeoForgeMod.NAMETAG_DISTANCE);
                event.put((EntityType<? extends LivingEntity>) holder.get(), builder.build());
            }
        });
    }
}
