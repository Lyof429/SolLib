package net.lcc.sollib;

import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.config.builder.IConfigurable;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SolTest {
    public static final SolModContainer MOD = new SolModContainer("SolTest", "soltest");

    public static void lyof() {
        ConfigEntry<String> hello = new ConfigEntry<>("world");
        ConfigEntry<Boolean> exists = new ConfigEntry<>(true);

        IConfigurable builder = it -> it
                .addObject("test_category", a -> a
                        .comment("This is a comment")
                        .add("hello", "minecraft:iron_pickaxe")
                        .bind(hello)
                        .addObject("nested", b -> b
                                .comment("Supports string, number and boolean values by default")
                                .add("exists", true)
                                .bind(exists))
                        .addObject("another", b -> b
                                .comment("Ah and lists of them too I forgot about that")
                                .comment("  (Lists don't have to hold a single type btw)")
                                .addArray("michel", c -> c
                                        .add(2)
                                        .add("this is a list, in case you didn't notice")
                                        .add(12))));
        MOD.createConfig("sollib/test", 1.0, builder);

        MOD.register(ItemHolder.class, "thingie", () -> new Item(new Item.Properties()))
                .withFuel(12).withTags(ItemTags.ANVIL).withModel(ModelTemplates.FLAT_ITEM);
        MOD.register(BlockHolder.class, "chaos_cube", () -> new Block(BlockBehaviour.Properties.of()))
                .withItem().withWall().dropsSelf();
    }

    public static void sasha() {
    }
}
