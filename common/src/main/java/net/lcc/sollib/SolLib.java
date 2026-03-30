package net.lcc.sollib;

import net.lcc.sollib.api.common.config.Configurable;
import net.lcc.sollib.api.common.config.JsonBuilder;
import net.lcc.sollib.api.common.logger.SolLogger;
import net.lcc.sollib.platform.Services;

public class SolLib {
    public static final String MOD_ID = "sollib";
    public static final SolLogger LOG = new SolLogger("SolLib");

    public static void init() {
        LOG.info(Services.PLATFORM.getConfigDirectory());
        JsonBuilder config = new JsonBuilder()
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
        LOG.info(config.toString());
        LOG.info(config.toJson());
    }
}