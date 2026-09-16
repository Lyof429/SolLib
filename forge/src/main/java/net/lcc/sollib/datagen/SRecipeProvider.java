package net.lcc.sollib.datagen;

import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class SRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public SRecipeProvider(PackOutput output) {
        super(output);
    }

    private static String getName(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        SolRegistries.MOD.iterate(BlockHolder.class, holder -> {
            if (holder.getStairs() != null) {
                stairBuilder(holder.getStairs().get(), Ingredient.of(holder.get()))
                        .group(getName(holder.getStairs().get()))
                        .unlockedBy(getHasName(holder.get()), has(holder.get()))
                        .save(consumer);
            }
            if (holder.getSlab() != null) {
                slabBuilder(RecipeCategory.BUILDING_BLOCKS, holder.getSlab().get(), Ingredient.of(holder.get()))
                        .group(getName(holder.getSlab().get()))
                        .unlockedBy(getHasName(holder.get()), has(holder.get()))
                        .save(consumer);
            }
            if (holder.getButton() != null) {
                buttonBuilder(holder.getButton().get(), Ingredient.of(holder.get()))
                        .group(getName(holder.getButton().get()))
                        .unlockedBy(getHasName(holder.get()), has(holder.get()))
                        .save(consumer);
            }
            if (holder.getPressurePlate() != null) {
                pressurePlateBuilder(RecipeCategory.REDSTONE, holder.getPressurePlate().get(), Ingredient.of(holder.get()))
                        .group(getName(holder.getPressurePlate().get()))
                        .unlockedBy(getHasName(holder.get()), has(holder.get()))
                        .save(consumer);
            }
        });
    }
}