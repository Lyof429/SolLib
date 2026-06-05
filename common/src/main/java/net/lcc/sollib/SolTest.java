package net.lcc.sollib;

import com.google.gson.JsonObject;
import net.lcc.sollib.api.client.SolClientRegistries;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.config.builder.IConfigurable;
import net.lcc.sollib.api.common.data.reload.SimpleDataRegistry;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.lcc.sollib.core.DebugItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SolTest {
    public static final SolModContainer MOD = new SolModContainer("SolLibTest", "sollib_test");

    public static final SimpleDataRegistry<String> messages = new SimpleDataRegistry<>("messages",
            (id, json) -> json.get("message").getAsString());

    public static void lyof() {
        ConfigEntry<String> hello = new ConfigEntry<>("world");
        ConfigEntry<JsonObject> exists = new ConfigEntry<>(new JsonObject());

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
                                .bind(exists)
                                .comment("Ah and lists of them too I forgot about that")
                                .comment("  (Lists don't have to hold a single type btw)")
                                .addArray("michel", c -> c
                                        .add(2)
                                        .add("this is a list, in case you didn't notice")
                                        .add(12))));
        MOD.createConfig("sollib/test", 1.0, builder);

        MOD.register(ItemHolder.class, "thing", () -> new Item(new Item.Properties()));
        MOD.register(BlockHolder.class, "cube", () -> new Block(BlockBehaviour.Properties.of())).withItem();

        MOD.register(ItemHolder.class, "debugger", () -> new DebugItem(new Item.Properties().stacksTo(1)));
    }


    public static void sasha() {
        SolClientRegistries.Render.BLOCK.register(state -> state.is(Blocks.BAMBOO) && state.getValue(BlockStateProperties.BAMBOO_LEAVES).equals(BambooLeaves.LARGE), (instance, state, pos, getter, poseStack, vertexConsumer, random) -> {
            BlockState state1 = Blocks.AZALEA.defaultBlockState();
            instance.renderBatched(state1, pos, getter, poseStack, vertexConsumer, true, random);
        });
    }
}
