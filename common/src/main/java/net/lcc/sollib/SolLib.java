package net.lcc.sollib;

import net.lcc.sollib.api.config.ConfigBuilder;
import net.lcc.sollib.api.logger.SolLogger;

public class SolLib {
    public static final String MOD_ID = "sollib";
    public static final SolLogger LOG = new SolLogger("SolLib");

    public static void init() {
        //LOG.info("Hello", Services.PLATFORM.getPlatformName(), "World!");
        ConfigBuilder config = new ConfigBuilder("sollib", 1.0)
                .push("test_category")
                    .comment("This is a comment")
                    .add("hello", "world")
                    .push("nested")
                        .comment("Supports string, number and boolean values by default")
                        .add("number", "hi")
                    .pop()
                    .push("another")
                        .comment("Ah and lists of them too I forgot about that")
                        .comment("  (Lists don't have to hold a single type btw)")
                        .addList("michel")
                            .add(2)
                            .add("this is a list, in case you didn't notice")
                            .add(12)
                        .pop()
                        .add("working", true)
                    .pop()
                .pop();
        LOG.info(config.build());

        /*
        {
          "solconfig": {
            "version": 1.0,
            "force_reset": false
          },

          // This config file uses a custom defined parser. That's why there are comments here, they wouldn't be valid in any other .json file
          //   To add a comment yourself, just start a line with // like here

          // TEST CATEGORY
          "test_category": {
            // This is a comment
            "hello": "world",
            "nested": {
              // Supports string, number and boolean values by default
              "number": "hi"
            },
            "another": {
              // Ah and lists of them too I forgot about that
              //   (Lists don't have to hold a single type btw)
              "michel": [
                2,
                "this is a list, in case you didn't notice",
                12
              ],
              "working": true
            }
          }
        }
         */
    }
}