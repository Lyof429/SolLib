package net.lcc.sollib.core;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import org.jetbrains.annotations.NotNull;

public class PotionRecipe implements IBrewingRecipe {
    public Holder<Potion> input;
    public Item ingredient;
    public Holder<Potion> output;

    public PotionRecipe(Holder<Potion> input, Item ingredient, Holder<Potion> output) {
        this.input = input;
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public boolean isInput(ItemStack input) {
        return input.has(DataComponents.POTION_CONTENTS) && input.get(DataComponents.POTION_CONTENTS).is(this.input);
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return ingredient.is(this.ingredient);
    }

    @Override
    public @NotNull ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (this.isInput(input) && this.isIngredient(ingredient)) {
            ItemStack result = input.copy();
            result.get(DataComponents.POTION_CONTENTS).withPotion(this.output);
            return result;
        }
        return ItemStack.EMPTY;
    }
}