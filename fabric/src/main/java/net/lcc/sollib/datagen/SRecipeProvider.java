package net.lcc.sollib.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class SRecipeProvider extends FabricRecipeProvider {
    public SRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    private static String getName(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        SolRegistries.MOD.iterate(BlockHolder.class, holder -> {
            if (holder.getStairs() != null) {
                stairBuilder(holder.getStairs().get(), Ingredient.of(holder.get()))
                        .group(getName(holder.getStairs().get()))
                        .unlockedBy(getHasName(holder.get()), has(holder.get()))
                        .save(exporter);
            }
            if (holder.getSlab() != null) {
                slabBuilder(RecipeCategory.BUILDING_BLOCKS, holder.getSlab().get(), Ingredient.of(holder.get()))
                        .group(getName(holder.getSlab().get()))
                        .unlockedBy(getHasName(holder.get()), has(holder.get()))
                        .save(exporter);
            }
            if (holder.getButton() != null) {
                buttonBuilder(holder.getButton().get(), Ingredient.of(holder.get()))
                        .group(getName(holder.getButton().get()))
                        .unlockedBy(getHasName(holder.get()), has(holder.get()))
                        .save(exporter);
            }
            if (holder.getPressurePlate() != null) {
                pressurePlateBuilder(RecipeCategory.REDSTONE, holder.getPressurePlate().get(), Ingredient.of(holder.get()))
                        .group(getName(holder.getPressurePlate().get()))
                        .unlockedBy(getHasName(holder.get()), has(holder.get()))
                        .save(exporter);
            }
        });
    }
}
