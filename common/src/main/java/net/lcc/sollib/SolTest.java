package net.lcc.sollib;

import net.lcc.sollib.api.client.SolClientRegistries;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.core.Identifier;
import net.minecraft.world.item.Items;

import java.io.IOException;

public class SolTest {
    public static final SolModContainer MOD = new SolModContainer("SolTest", "soltest");

    public static void lyof() {
        SolLib.MOD.createConfig("soltest", 1, builder -> builder.add("thing", true));

        SolRegistries.Data.RELOAD.register(manager -> {
            var resource = manager.getResourceStack(Identifier.of("minecraft", "recipes/anvil.json"));
            resource.forEach(r -> {
                try {
                    MOD.getLogger().info(r.sourcePackId(), new String(r.open().readAllBytes()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }


    public static void sasha() {
    }
}
