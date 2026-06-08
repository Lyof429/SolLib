package net.lcc.sollib.api.client.model.item;

import net.lcc.sollib.core.Identifier;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SItemModelRegistry {
    public static final SItemModelRegistry INSTANCE = new SItemModelRegistry();
    private SItemModelRegistry() {}

    private final Map<Item, ModelResourceLocation> INSTANCES = new HashMap<>();

    /**
     * Registers an item to have a different held model
     */
    public void register(ItemLike item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item.asItem());
        INSTANCES.putIfAbsent(item.asItem(),
                new ModelResourceLocation(Identifier.of(id.getNamespace(), id.getPath() + "_in_hand"), "inventory"));
    }

    @ApiStatus.Internal
    public void load(Consumer<ModelResourceLocation> loader) {
        for (ModelResourceLocation model : INSTANCES.values())
            loader.accept(model);
    }

    @ApiStatus.Internal
    @Nullable
    public ModelResourceLocation getModel(ItemStack stack) {
        return INSTANCES.get(stack.getItem());
    }
}
