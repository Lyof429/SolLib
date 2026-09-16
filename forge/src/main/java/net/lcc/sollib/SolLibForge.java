package net.lcc.sollib;

import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.EffectHolder;
import net.lcc.sollib.api.common.registry.holder.EntityHolder;
import net.lcc.sollib.core.Identifier;
import net.lcc.sollib.core.PotionRecipe;
import net.lcc.sollib.datagen.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Mod(SolLib.MOD_ID)
@Mod.EventBusSubscriber(modid = SolLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SolLibForge {
    public SolLibForge() {
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

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        BlockTagsProvider blockTagsProvider = new SBlockTagProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new SItemTagProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new SEntityTagProvider(packOutput, lookupProvider, existingFileHelper));

        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(SBlockLootTableProvider::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(SEntityLootTableProvider::new, LootContextParamSets.ENTITY))));

        generator.addProvider(event.includeServer(), new SRecipeProvider(packOutput));

        generator.addProvider(event.includeClient(), new SItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new SBlockStateProvider(packOutput, existingFileHelper));
    }
}
