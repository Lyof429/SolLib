package net.lcc.sollib.api.common.registry.holder;

import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.api.common.registry.data.BlockModel;
import net.lcc.sollib.api.common.registry.data.Flammability;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockHolder extends Holder<Block> {
    private ItemHolder item;
    private List<TagKey<Block>> tags;
    private Supplier<ItemLike> drop;
    private NumberProvider dropCount;
    private BlockModel model;
    private boolean cutout;
    private Flammability flammability;
    private Supplier<Block> stripResult;

    public BlockHolder(SolModContainer mod, String name, Supplier<Block> entrySupplier) {
        super(mod, name, entrySupplier);

        this.item = null;
        this.tags = List.of();
        this.drop = null;
        this.dropCount = null;
        this.model = null;
        this.cutout = false;
        this.flammability = null;
        this.stripResult = null;
    }

    public static Registry<?> getRegistryType() {
        return BuiltInRegistries.BLOCK;
    }


    public BlockHolder withItem() {
        this.item = this.mod.getRegistrar(ItemHolder.class)
                .register(this.name, () -> new BlockItem(this.get(), new Item.Properties()));
        return this;
    }

    public BlockHolder withItem(Consumer<ItemHolder> consumer) {
        this.withItem();
        consumer.accept(this.item);
        return this;
    }


    /**
     * Registers this item to the supplied tags
     * @param tags the tag keys to register the item to
     */
    @SafeVarargs
    public final BlockHolder withTags(TagKey<Block>... tags) {
        this.tags = List.of(tags);
        return this;
    }

    public List<TagKey<Block>> getTags() {
        return this.tags;
    }


    public Supplier<ItemLike> getDrop() {
        return this.drop;
    }

    public NumberProvider getDropCount() {
        return this.dropCount;
    }

    public BlockHolder dropsSelf() {
        return this.dropsOther(this::get);
    }

    public final BlockHolder dropsOther(Supplier<ItemLike> drop) {
        return this.dropsOther(drop, 1);
    }

    public final BlockHolder dropsOther(Supplier<ItemLike> drop, int count) {
        return this.dropsOther(drop, ConstantValue.exactly(count));
    }

    public final BlockHolder dropsOther(Supplier<ItemLike> drop, NumberProvider count) {
        this.drop = drop;
        this.dropCount = count;
        return this;
    }

    public BlockHolder dropsWithSilk() {
        this.drop = this::get;
        this.dropCount = null;
        return this;
    }

    public boolean hasDrop() {
        return this.drop != null;
    }


    /**
     * The model type of this block for datagen
     * If any of the blocksets are added to this block, this value will be ignored and CUBE will be used
     */
    public BlockHolder withModel(BlockModel model) {
        this.model = model;
        return this;
    }

    public boolean hasModel() {
        return this.model != null;
    }

    public BlockModel getModel() {
        return this.model;
    }


    public BlockHolder cutout() {
        this.cutout = true;
        return this;
    }

    public boolean isCutout() {
        return this.cutout;
    }


    public BlockHolder withFlammability(int ignite, int spread) {
        this.flammability = new Flammability(ignite, spread);
        return this;
    }

    public Flammability getFlammability() {
        return this.flammability;
    }


    /**
     * Adds the ability to strip this block with an axe
     * @param stripResult the block to set it to when it gets stripped
     */
    public BlockHolder withStripResult(Supplier<Block> stripResult) {
        this.stripResult = stripResult;
        return this;
    }

    public boolean hasStripResult() {
        return this.stripResult != null;
    }

    public Supplier<Block> getStripResult() {
        return this.stripResult;
    }
}
