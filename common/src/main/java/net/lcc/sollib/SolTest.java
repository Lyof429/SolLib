package net.lcc.sollib;

import net.lcc.sollib.api.common.config.Configurable;
import net.lcc.sollib.api.common.config.JsonBuilder;
import net.lcc.sollib.api.common.config.SolConfig;

public class SolTest {
    public record Thing(int x, String name) implements Configurable {
        @Override
        public void toJson(JsonBuilder builder) {
            builder.add("name", name).add("x", x);
        }
    }

    public static void init() {
        Configurable builder = it -> it
                .addCategory("test_category", a -> a
                        .comment("This is a comment")
                        .add("hello", "world")
                        .bind(null)
                        .addCategory("nested", b -> b
                                .comment("Supports string, number and boolean values by default")
                                .bind(null)
                                .add("exists", true))
                        .addCategory("another", b -> b
                                .bind(null)
                                .comment("Ah and lists of them too I forgot about that")
                                .comment("  (Lists don't have to hold a single type btw)")
                                .addList("michel", c -> c
                                        .add(2)
                                        .add("this is a list, in case you didn't notice")
                                        .addCategory(new Thing(7, "luigi"))
                                        .add(12))))
                .addCategory("thing", new Thing(3, "mario"));
        SolConfig config = new SolConfig("sollib/test", 1.0, builder);
        config.init();
    }
}
