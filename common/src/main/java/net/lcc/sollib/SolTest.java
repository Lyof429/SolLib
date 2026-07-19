package net.lcc.sollib;

import net.lcc.sollib.api.common.registry.SolModContainer;

public class SolTest {
    public static final SolModContainer MOD = new SolModContainer("SolTest", "soltest");

    public static void lyof() {
        SolLib.MOD.createConfig("soltest", 1, builder -> builder
                .addObject("main", main -> main
                        .comment("This needs testing")
                        .add("text", "hello world")
                        .comment("Here be the universe")
                        .add("answer", 42)
                )
        );
    }


    public static void sasha() {
    }
}
