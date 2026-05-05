package net.lcc.sollib.api.common.registry.holder;

import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A holder class for effect registry using a {@link SolModContainer}
 * @author Cocreated by Hellion and Lyof
 */
public class EffectHolder extends Holder<MobEffect> {
    public static final int DURATION = 3600;

    private Supplier<Potion> craftingBase;
    private Supplier<ItemLike> craftingIngredient;
    private Supplier<Potion> potion;
    private Supplier<Potion> longPotion;
    private Supplier<Potion> strongPotion;
    private int potionDuration;

    public EffectHolder(SolModContainer mod, String name, Supplier<MobEffect> entrySupplier) {
        super(mod, name, entrySupplier);

        this.craftingBase = null;
        this.craftingIngredient = null;
        this.potion = null;
        this.longPotion = null;
        this.strongPotion = null;
        this.potionDuration = 0;
    }

    @Override
    protected Registry<MobEffect> getRegistry() {
        return BuiltInRegistries.MOB_EFFECT;
    }

    /**
     * Registers a potion for this effect
     * @param ingredient What ingredient must be added to brew this
     */
    public EffectHolder withPotion(Supplier<ItemLike> ingredient) {
        return this.withPotion(() -> Potions.AWKWARD, ingredient);
    }

    /**
     * Registers a potion for this effect
     * @param base What potion this is brewed from (eg {@link Potions#AWKWARD})
     * @param ingredient What ingredient must be added to brew this
     */
    public EffectHolder withPotion(Supplier<Potion> base, Supplier<ItemLike> ingredient) {
        return this.withPotion(base, ingredient, DURATION);
    }

    /**
     * Registers a potion for this effect
     * @param base What potion this is brewed from (eg {@link Potions#AWKWARD})
     * @param ingredient What ingredient must be added to brew this
     * @param duration The duration for said potion
     */
    public EffectHolder withPotion(Supplier<Potion> base, Supplier<ItemLike> ingredient, int duration) {
        return this.withPotion(base, ingredient, duration, true, true);
    }

    /**
     * Registers a potion for this effect
     * @param base What potion this is brewed from (eg {@link Potions#AWKWARD})
     * @param ingredient What ingredient must be added to brew this
     * @param duration The duration for said potion
     * @param hasLong Should a long variant be registered too
     * @param hasStrong Should a strong variant be registered too
     */
    public EffectHolder withPotion(Supplier<Potion> base, Supplier<ItemLike> ingredient, int duration,
                                   boolean hasLong, boolean hasStrong) {
        this.craftingBase = base;
        this.craftingIngredient = ingredient;
        this.potion = () -> new Potion(new MobEffectInstance(this.get(), this.potionDuration));
        String name = this.getID().getPath();
        this.longPotion = hasLong ? () -> new Potion(name, new MobEffectInstance(this.get(),
                this.potionDuration * 8 / 3)) : null;
        this.strongPotion = hasStrong ? () -> new Potion(name, new MobEffectInstance(this.get(),
                this.potionDuration / 2, 1)) : null;
        this.potionDuration = duration;

        if (Services.PLATFORM.getPlatformName().equals("Fabric"))
            this.registerPotion((id, potion) -> Registry.register(BuiltInRegistries.POTION, id, potion.get()));

        return this;
    }

    public boolean hasPotion() {
        return this.potion != null;
    }

    public boolean hasLongPotion() {
        return this.hasPotion() && this.longPotion != null;
    }

    public boolean hasStrongPotion() {
        return this.hasPotion() && this.strongPotion != null;
    }

    public Supplier<Potion> getCraftingBase() {
        return this.craftingBase;
    }

    public Supplier<ItemLike> getCraftingIngredient() {
        return this.craftingIngredient;
    }

    public Supplier<Potion> getPotion() {
        return this.potion;
    }

    public Supplier<Potion> getLongPotion() {
        return this.longPotion;
    }

    public Supplier<Potion> getStrongPotion() {
        return this.strongPotion;
    }

    @ApiStatus.Internal
    public void registerPotion(BiConsumer<ResourceLocation, Supplier<Potion>> registrar) {
        registrar.accept(this.getID(), this.potion);

        String name = this.getID().getPath();
        if (this.longPotion != null)
            registrar.accept(this.mod.makeID("long_" + name), this.longPotion);
        if (this.strongPotion != null)
            registrar.accept(this.mod.makeID("strong_" + name), this.strongPotion);
    }
}
