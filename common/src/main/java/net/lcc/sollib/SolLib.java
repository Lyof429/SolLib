package net.lcc.sollib;

import net.lcc.sollib.api.common.config.Configurable;
import net.lcc.sollib.api.common.config.JsonBuilder;
import net.lcc.sollib.api.common.logger.SolLogger;

public class SolLib {
    public static final String MOD_ID = "sollib";
    public static final SolLogger LOG = new SolLogger("SolLib");

    public record Test(int x, String name) implements Configurable {
        @Override
        public JsonBuilder toConfigEntry() {
            return new JsonBuilder().add("x", this.x()).add("name", this.name());
        }
    }

    public static void init() {
        //LOG.info("Hello", Services.PLATFORM.getPlatformName(), "World!");
        JsonBuilder config = new JsonBuilder()
                .addCategory("test_category", a -> a
                    .comment("This is a comment")
                    .add("hello", "world")
                    .bind(null)
                    .addCategory("nested", b -> b
                        .comment("Supports string, number and boolean values by default")
                        .add("number", new Test(3, "mario"))
                        .bind(null)
                        .add("afterobject", 2.3))
                    .addCategory("another", b -> b
                        .bind(null)
                        .comment("Ah and lists of them too I forgot about that")
                        .comment("  (Lists don't have to hold a single type btw)")
                        .addList("michel", c -> c
                            .add(2)
                            .add("this is a list, in case you didn't notice")
                            .addList(d -> d.
                                    add("ayaya")
                                .addCategory(e -> e
                                    .add("thing", false)))
                            .add(new Test(31, "luigi"))
                            .add(12))
                        .add("working", true)))
                .addCategory("patrick", a -> a
                    .add("idkwhattowrite", 42));
        LOG.info(config.toString());
        LOG.info(config.toJson());
    }
}