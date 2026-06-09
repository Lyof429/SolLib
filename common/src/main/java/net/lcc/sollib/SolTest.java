package net.lcc.sollib;

import net.lcc.sollib.api.client.SolClientRegistries;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.minecraft.world.item.Items;

public class SolTest {
    public static final SolModContainer MOD = new SolModContainer("SolTest", "soltest");

    public static void lyof() {
        SolClientRegistries.ITEM_MODEL.registerHeld(Items.DIAMOND);
    }


    public static void sasha() {
    }
}
