package net.lcc.sollib;

import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.*;
import net.lcc.sollib.core.PotionRecipe;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Map;
import java.util.function.Supplier;

@Mod(SolLib.MOD_ID)
@Mod.EventBusSubscriber(modid = SolLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SolLibForge {
    public SolLibForge() {
        SolLib.init();
    }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        SolRegistries.MOD.iterate(ItemHolder.class, holder -> event.register(Registries.ITEM, holder.getID(), holder));
        SolRegistries.MOD.iterate(BlockHolder.class, holder -> event.register(Registries.BLOCK, holder.getID(), holder));
        SolRegistries.MOD.iterate(EntityHolder.class, holder -> event.register(Registries.ENTITY_TYPE, holder.getID(), holder));
        SolRegistries.MOD.iterate(EffectHolder.class, holder -> event.register(Registries.MOB_EFFECT, holder.getID(), holder));
        SolRegistries.MOD.iterate(EffectHolder.class, holder -> holder.registerPotion(
                potion -> event.register(Registries.POTION, potion.getID(), potion)
        ));
        SolRegistries.MOD.iterate(DensityFunctionHolder.class, holder -> event.register(Registries.DENSITY_FUNCTION_TYPE, holder.getID(), holder));

        SolRegistries.MOD.iterate(EntityHolder.class, holder -> {
            if (holder.shouldSpawn()) {
                ResourceLocation id = holder.getID();

                SolRegistries.Data.RUNTIME.addJson(ResourceLocation.tryBuild(id.getNamespace(),
                                "tags/worldgen/biome/" + id.getPath() + "_can_spawn.json"),
                        json -> holder.getSpawn().createTag(json, id));
                SolRegistries.Data.RUNTIME.addJson(ResourceLocation.tryBuild(id.getNamespace(),
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
                builder.add(ForgeMod.SWIM_SPEED.get())
                        .add(ForgeMod.NAMETAG_DISTANCE.get())
                        .add(ForgeMod.ENTITY_GRAVITY.get());
                event.put((EntityType<? extends LivingEntity>) holder.get(), builder.build());
            }
        });
    }

    @SubscribeEvent
    public static void register(FMLCommonSetupEvent event) {
        SolRegistries.MOD.iterate(EffectHolder.class, holder -> {
            if (holder.hasPotion()) {
                BrewingRecipeRegistry.addRecipe(new PotionRecipe(holder.getCraftingBase().get(), holder.getCraftingIngredient().get().asItem(), holder.getPotion().get()));

                if (holder.hasLongPotion())
                    BrewingRecipeRegistry.addRecipe(new PotionRecipe(holder.getPotion().get(), Items.REDSTONE, holder.getLongPotion().get()));
                if (holder.hasStrongPotion())
                    BrewingRecipeRegistry.addRecipe(new PotionRecipe(holder.getPotion().get(), Items.GLOWSTONE_DUST, holder.getStrongPotion().get()));
            }
        });
    }
}
