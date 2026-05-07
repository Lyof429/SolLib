package net.lcc.sollib.api.common.registry.holder;

import net.lcc.sollib.api.common.registry.Holder;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

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
    private Holder<Potion> potion;
    private Holder<Potion> longPotion;
    private Holder<Potion> strongPotion;

    public EffectHolder(SolModContainer mod, String name, Supplier<MobEffect> entrySupplier) {
        super(mod, name, entrySupplier);

        this.craftingBase = null;
        this.craftingIngredient = null;
        this.potion = null;
        this.longPotion = null;
        this.strongPotion = null;
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

        this.potion = new Holder<>(this.mod, this.name, () -> new Potion(new MobEffectInstance(this.get(), duration)));
        this.longPotion = hasLong ? new Holder<>(this.mod, "long_" + this.name,
                () -> new Potion(name, new MobEffectInstance(this.get(), duration * 8 / 3))) : null;
        this.strongPotion = hasStrong ? new Holder<>(this.mod, "strong_" + this.name,
                () -> new Potion(name, new MobEffectInstance(this.get(), duration / 2, 1))) : null;

        if (Services.PLATFORM.getPlatformName().equals("Fabric"))
            this.registerPotion(holder -> Registry.register(BuiltInRegistries.POTION, holder.getID(), holder.get()));

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
    public void registerPotion(Consumer<Holder<Potion>> registrar) {
        registrar.accept(this.potion);
        if (this.longPotion != null)
            registrar.accept(this.longPotion);
        if (this.strongPotion != null)
            registrar.accept(this.strongPotion);
    }
}
