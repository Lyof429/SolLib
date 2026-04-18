package net.lcc.sollib.api.common.registry;

import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A holder class for item registry using a {@link SolRegistrar}
 * @author Cocreated by Hellion and Lyof
 */
public class ItemHolder extends Holder<Item> {
    private static final Map<TagKey<Item>, List<ItemHolder>> TAGS = new HashMap<>();

    private ModelTemplate model;
    private int fuelDuration;

    public ItemHolder(Supplier<Item> entrySupplier) {
        super(entrySupplier);
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
        for (TagKey<Item> tag : tags) {
            TAGS.putIfAbsent(tag, new ArrayList<>());
            TAGS.get(tag).add(this);
        }

        return this;
    }

    public static Map<TagKey<Item>, List<ItemHolder>> getTags() {
        return TAGS;
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
