package net.lcc.sollib.api.common.registry.holder;

import net.lcc.sollib.api.common.registry.SolModContainer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;

/**
 * A holder class for item registry using a {@link SolModContainer}
 * @author Cocreated by Hellion and Lyof
 */
public class ItemHolder extends Holder<Item> {
    private int fuelDuration;
    private List<TagKey<Item>> tags;
    private ModelTemplate model;

    public ItemHolder(SolModContainer mod, String name, Supplier<Item> entrySupplier) {
        super(mod, name, entrySupplier);

        this.tags = List.of();
        this.model = null;
        this.fuelDuration = 0;
    }

    @Override
    protected Registry<Item> getRegistry() {
        return BuiltInRegistries.ITEM;
    }

    /**
     * Registers the item as a fuel source
     * @param fuelDuration the length in ticks this fuel source burns
     */
    public ItemHolder withFuel(int fuelDuration) {
        this.fuelDuration = fuelDuration;
        return this;
    }

    public boolean isFuel() {
        return this.fuelDuration > 0;
    }

    public int getFuelDuration() {
        return this.fuelDuration;
    }

    /**
     * Registers this item to the supplied tags
     * @param tags the tag keys to register the item to
     */
    @SafeVarargs
    public final ItemHolder withTags(TagKey<Item>... tags) {
        this.tags = List.of(tags);
        return this;
    }

    public List<TagKey<Item>> getTags() {
        return this.tags;
    }

    /**
     * The model type of this item for datagen
     */
    public ItemHolder withModel(ModelTemplate model) {
        this.model = model;
        return this;
    }

    public boolean hasModel() {
        return this.model != null;
    }

    public ModelTemplate getModel() {
        return this.model;
    }
}
