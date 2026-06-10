package net.lcc.sollib;

import net.lcc.sollib.api.client.SolClientRegistries;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.config.builder.IConfigurable;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.lcc.sollib.core.Identifier;
import net.lcc.sollib.platform.Services;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SolTest {
    public static final SolModContainer MOD = new SolModContainer("SolTest", "soltest");

    public static void lyof() {
        ConfigEntry<String> hello = new ConfigEntry<>("world");
        ConfigEntry<Boolean> exists = new ConfigEntry<>(true);

        IConfigurable builder = it -> it
                .addObject("category", a -> a
                        .comment("This is a comment")
                        .add("hello", "world")
                        .add("number", 5)
                        );
        MOD.createConfig("sollib/test", 1.0, builder);

        MOD.register(ItemHolder.class, "thingie", () -> new Item(new Item.Properties()))
                .withFuel(12)
                .withTags(ItemTags.ANVIL)
                .withModel(ModelTemplates.FLAT_ITEM);
        MOD.register(BlockHolder.class, "block_block", () -> new Block(BlockBehaviour.Properties.of()))
                .withItem(item -> item.withFuel(200))
                .withSlab().withStairs().withWall()
                .withFlammability(20, 5)
                .dropsSelf();

        SolRegistries.Data.RUNTIME.addJson(Identifier.of("minecraft", "recipe/dropper"), json -> {
            if (json == null) return null;

            json.getAsJsonObject("key")
                    .getAsJsonObject("#")
                    .addProperty("item", "minecraft:copper_ingot");
            return json;
        });
    }

    public static void sasha() {
    }
}
