package net.lcc.sollib;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.config.Configurable;
import net.lcc.sollib.api.common.config.JsonBuilder;
import net.lcc.sollib.api.common.config.SolConfig;

public class SolTest {
    public record Thing(double x, String name) implements Configurable {
        @Override
        public void toJson(JsonBuilder builder) {
            builder.add("name", name).add("x", x);
        }

        @Override
        public String toString() {
            return "Thing{" +
                    "x=" + x +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static void lyof() {
        ConfigEntry<String> hello = new ConfigEntry<>("world");
        ConfigEntry<Boolean> exists = new ConfigEntry<>(true);
        ConfigEntry<Thing> another = new ConfigEntry<>(new Thing(-4, "error")).withProcessor(elm ->
                new Thing(elm.getAsJsonObject().get("x").getAsDouble(), elm.getAsJsonObject().get("name").getAsString()));

        Configurable builder = it -> it
                .addCategory("test_category", a -> a
                        .comment("This is a comment")
                        .add("hello", "world")
                        .bind(hello)
                        .addCategory("nested", b -> b
                                .comment("Supports string, number and boolean values by default")
                                .add("exists", true)
                                .bind(exists))
                        .addCategory("another", b -> b
                                .comment("Ah and lists of them too I forgot about that")
                                .comment("  (Lists don't have to hold a single type btw)")
                                .addList("michel", c -> c
                                        .add(2)
                                        .add("this is a list, in case you didn't notice")
                                        .addCategory(new Thing(7, "luigi"))
                                        .add(12))))
                .addCategory("thing", new Thing(3, "mario"))
                .bind(another);
        SolConfig config = new SolConfig("sollib/test", 1.0, builder) {
            @Override
            public void init() {
                super.init();
                SolLib.LOG.info(hello.get(), exists.get(), another.get().name(),
                        new ConfigEntry<>("sollib/test", "test_category.another.michel", new JsonArray()).get());
            }
        };
        config.init();
    }
}
