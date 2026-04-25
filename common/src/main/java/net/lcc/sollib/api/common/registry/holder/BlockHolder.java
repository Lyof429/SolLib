package net.lcc.sollib.api.common.registry.holder;

import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.api.common.registry.data.BlockModel;
import net.lcc.sollib.api.common.registry.data.Flammability;
import net.lcc.sollib.mixin.access.ButtonBlockAccessor;
import net.lcc.sollib.mixin.access.PressurePlateBlockAccessor;
import net.lcc.sollib.mixin.access.StairBlockAccessor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final Map<BlockModel, BlockHolder> blockset;

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
        this.blockset = new HashMap<>();
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
        if (this.item == null) this.withItem();
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
     * If any of the blockset are added to this block, this value will be ignored and CUBE will be used
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

    public boolean isFlammable() {
        return this.flammability != null;
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


    public BlockHolder withStairs() {
        BlockHolder stairs = this.mod.getRegistrar(BlockHolder.class).register(this.name + "_stairs",
                        () -> StairBlockAccessor.createStairBlock(this.get().defaultBlockState(), BlockBehaviour.Properties.copy(this.get())))
                .withModel(BlockModel.STAIRS)
                .withItem(it -> it.withTags(ItemTags.STAIRS))
                .withTags(BlockTags.STAIRS);
        this.blockset.put(BlockModel.STAIRS, stairs);
        return this;
    }

    public BlockHolder withStairs(Consumer<BlockHolder> consumer) {
        if (this.getStairs() == null) this.withStairs();
        consumer.accept(this.getStairs());
        return this;
    }

    public BlockHolder getStairs() {
        return this.blockset.get(BlockModel.STAIRS);
    }

    public BlockHolder withSlab() {
        BlockHolder slab = this.mod.getRegistrar(BlockHolder.class).register(this.name + "_slab",
                        () -> new SlabBlock(BlockBehaviour.Properties.copy(this.get())))
                .withModel(BlockModel.SLAB)
                .withItem(it -> it.withTags(ItemTags.SLABS))
                .withTags(BlockTags.SLABS);
        this.blockset.put(BlockModel.SLAB, slab);
        return this;
    }

    public BlockHolder withSlab(Consumer<BlockHolder> consumer) {
        if (this.getSlab() == null) this.withSlab();
        consumer.accept(this.getSlab());
        return this;
    }

    public BlockHolder getSlab() {
        return this.blockset.get(BlockModel.SLAB);
    }

    public BlockHolder withWall() {
        BlockHolder wall = this.mod.getRegistrar(BlockHolder.class).register(this.name + "_wall",
                        () -> new WallBlock(BlockBehaviour.Properties.copy(this.get())))
                .withModel(BlockModel.WALL)
                .withItem(it -> it.withTags(ItemTags.WALLS))
                .withTags(BlockTags.WALLS);
        this.blockset.put(BlockModel.WALL, wall);
        return this;
    }

    public BlockHolder withWall(Consumer<BlockHolder> consumer) {
        if (this.getWall() == null) this.withWall();
        consumer.accept(this.getWall());
        return this;
    }

    public BlockHolder getWall() {
        return this.blockset.get(BlockModel.WALL);
    }

    public BlockHolder withButton(BlockSetType type, int ticksPressed, boolean arrowCanPress) {
        BlockHolder button = this.mod.getRegistrar(BlockHolder.class).register(this.name + "_button", 
                        () -> ButtonBlockAccessor.createButtonBlock(BlockBehaviour.Properties.copy(this.get()), type, ticksPressed, arrowCanPress))
                .withModel(BlockModel.BUTTON)
                .withItem(it -> it.withTags(ItemTags.BUTTONS))
                .withTags(BlockTags.BUTTONS);
        this.blockset.put(BlockModel.BUTTON, button);
        return this;
    }

    public BlockHolder withButton(Consumer<BlockHolder> consumer) {
        consumer.accept(this.getButton());
        return this;
    }

    public BlockHolder getButton() {
        return this.blockset.get(BlockModel.BUTTON);
    }

    public BlockHolder withPressurePlate(PressurePlateBlock.Sensitivity sensitivity, BlockSetType type) {
        BlockHolder pressurePlate = this.mod.getRegistrar(BlockHolder.class).register(this.name + "_pressure_plate",
                        () -> PressurePlateBlockAccessor.createPressurePlateBlock(sensitivity, BlockBehaviour.Properties.copy(this.get()), type))
                .withModel(BlockModel.PRESSURE_PLATE)
                .withItem()
                .withTags(BlockTags.PRESSURE_PLATES);
        this.blockset.put(BlockModel.PRESSURE_PLATE, pressurePlate);
        return this;
    }

    public BlockHolder withPressurePlate(Consumer<BlockHolder> consumer) {
        consumer.accept(this.getPressurePlate());
        return this;
    }

    public BlockHolder getPressurePlate() {
        return this.blockset.get(BlockModel.PRESSURE_PLATE);
    }

    public BlockHolder withFence() {
        BlockHolder fence = this.mod.getRegistrar(BlockHolder.class).register(this.name + "_fence",
                        () -> new FenceBlock(BlockBehaviour.Properties.copy(this.get())))
                .withModel(BlockModel.FENCE)
                .withItem(it -> it.withTags(ItemTags.FENCES))
                .withTags(BlockTags.FENCES);
        this.blockset.put(BlockModel.FENCE, fence);
        return this;
    }

    public BlockHolder withFence(Consumer<BlockHolder> consumer) {
        if (this.getFence() == null) this.withFence();
        consumer.accept(this.getFence());
        return this;
    }

    public BlockHolder getFence() {
        return this.blockset.get(BlockModel.FENCE);
    }

    public BlockHolder withFenceGate(WoodType woodType) {
        BlockHolder fenceGate = this.mod.getRegistrar(BlockHolder.class).register(this.name + "_fence_gate",
                        () -> new FenceGateBlock(BlockBehaviour.Properties.copy(this.get()), woodType))
                .withModel(BlockModel.FENCE_GATE)
                .withItem(it -> it.withTags(ItemTags.FENCE_GATES))
                .withTags(BlockTags.FENCE_GATES);
        this.blockset.put(BlockModel.FENCE_GATE, fenceGate);
        return this;
    }

    public BlockHolder withFenceGate(Consumer<BlockHolder> consumer) {
        consumer.accept(this.getFenceGate());
        return this;
    }

    public BlockHolder getFenceGate() {
        return this.blockset.get(BlockModel.FENCE_GATE);
    }

    public Map<BlockModel, BlockHolder> getBlockSet() {
        return this.blockset;
    }
}
