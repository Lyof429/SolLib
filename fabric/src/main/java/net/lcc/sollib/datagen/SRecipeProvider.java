package net.lcc.sollib.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.function.Consumer;

public class SRecipeProvider extends FabricRecipeProvider {
    public SRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    private static String getName(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> exporter) {
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
