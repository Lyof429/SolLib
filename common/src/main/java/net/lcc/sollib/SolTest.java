package net.lcc.sollib;

import net.lcc.sollib.api.SolModContainer;
import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.config.IConfigurable;
import net.lcc.sollib.api.common.config.SolConfig;
import net.lcc.sollib.api.common.registry.ItemHolder;
import net.lcc.sollib.api.common.registry.SolRegistrar;
import net.minecraft.world.item.Item;

public class SolTest {
    public static SolConfig CONFIG;
    public static SolModContainer MOD = new SolModContainer("sollib");

    public static void lyof() {
        ConfigEntry<String> hello = new ConfigEntry<>("world");
        ConfigEntry<Boolean> exists = new ConfigEntry<>(true);

        IConfigurable builder = it -> it
                .addObject("test_category", a -> a
                        .comment("This is a comment")
                        .add("hello", "world")
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
        CONFIG = new SolConfig("sollib/test", 1.0, builder);
        CONFIG.init();


        SolRegistrar<ItemHolder> items = MOD.getRegistrar(ItemHolder.class);

        ItemHolder x = items.register("test", () -> new Item(new Item.Properties()))
                .withFuel(5);
    }


    public static void sasha() {
    }
}
