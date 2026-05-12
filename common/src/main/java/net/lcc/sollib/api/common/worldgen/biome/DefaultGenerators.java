package net.lcc.sollib.api.common.worldgen.biome;

import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.core.Identifier;

public class DefaultGenerators {
    /**
     * Makes sure the vanilla dimensions are properly data driven if needed (because for some reason they're not)
     */
    public static void init() {
        SolRegistries.Data.RUNTIME.addRemoval(Identifier.of("minecraft", "dimension/overworld.json"),
                () -> !SolRegistries.BIOME.has("minecraft:overworld"));

        SolRegistries.Data.RUNTIME.addRemoval(Identifier.of("minecraft", "dimension/the_nether.json"),
                () -> !SolRegistries.BIOME.has("minecraft:the_nether"));

        SolRegistries.Data.RUNTIME.addRemoval(Identifier.of("minecraft", "dimension/the_end.json"),
                () -> !SolRegistries.BIOME.has("minecraft:the_end"));
    }
}
