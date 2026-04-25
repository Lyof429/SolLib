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
        SolRegistries.MOD.apply(BlockHolder.class, (registry, id, it) -> {
            if (it.getStairs() != null) {
                stairBuilder(it.getStairs().get(), Ingredient.of(it.get()))
                        .group(getName(it.getStairs().get()))
                        .unlockedBy(getHasName(it.get()), has(it.get()))
                        .save(exporter);
            }
            if (it.getSlab() != null) {
                slabBuilder(RecipeCategory.BUILDING_BLOCKS, it.getSlab().get(), Ingredient.of(it.get()))
                        .group(getName(it.getSlab().get()))
                        .unlockedBy(getHasName(it.get()), has(it.get()))
                        .save(exporter);
            }
            if (it.getButton() != null) {
                buttonBuilder(it.getButton().get(), Ingredient.of(it.get()))
                        .group(getName(it.getButton().get()))
                        .unlockedBy(getHasName(it.get()), has(it.get()))
                        .save(exporter);
            }
            if (it.getPressurePlate() != null) {
                pressurePlateBuilder(RecipeCategory.REDSTONE, it.getPressurePlate().get(), Ingredient.of(it.get()))
                        .group(getName(it.getPressurePlate().get()))
                        .unlockedBy(getHasName(it.get()), has(it.get()))
                        .save(exporter);
            }
        });
    }
}
