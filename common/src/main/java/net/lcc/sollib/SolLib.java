package net.lcc.sollib;

import net.lcc.sollib.api.config.ConfigBuilder;
import net.lcc.sollib.api.logger.SolLogger;

public class SolLib {
    public static final String MOD_ID = "sollib";
    public static final SolLogger LOG = new SolLogger("SolLib");

    public static void init() {
        //LOG.info("Hello", Services.PLATFORM.getPlatformName(), "World!");
        ConfigBuilder config = new ConfigBuilder(1.0)
                .category("test_category", a -> a
                    .comment("This is a comment")
                    .entry("hello", "world")
                    .bind(null)
                    .category("nested", b -> b
                        .comment("Supports string, number and boolean values by default")
                        .entry("number", "hi")
                        .bind(null))
                    .category("another", b -> b
                        .bind(null)
                        .comment("Ah and lists of them too I forgot about that")
                        .comment("  (Lists don't have to hold a single type btw)")
                        .list("michel", c -> c
                            .entry(2)
                            .entry("this is a list, in case you didn't notice")
                            .list(d -> d.
                                entry("ayaya")
                                .category(e -> e
                                    .entry("thing", false)))
                            .entry(12))
                        .entry("working", true)))
                .category("patrick", a -> a
                    .entry("idkwhattowrite", 42));
        LOG.info(config.build("sollib"));
    }
}