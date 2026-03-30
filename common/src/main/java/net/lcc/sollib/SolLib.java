package net.lcc.sollib;

import net.lcc.sollib.api.common.config.JsonBuilder;
import net.lcc.sollib.api.common.config.SolConfig;
import net.lcc.sollib.api.common.config.SolConfigRegistry;
import net.lcc.sollib.api.common.logger.SolLogger;

import java.util.function.Consumer;

public class SolLib {
    public static final String MOD_ID = "sollib";
    public static final SolLogger LOG = new SolLogger("SolLib");

    public static void init() {
        Consumer<JsonBuilder> builder = it -> it
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
                            .add(12))));
        SolConfig config = new SolConfig("test/sollib", 1.0, builder);
        SolConfigRegistry.reload();
    }
}