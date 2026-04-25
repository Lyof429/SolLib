package net.lcc.sollib;

import net.fabricmc.api.ModInitializer;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.ItemHolder;
import net.minecraft.core.Registry;

public class SolLibFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SolLib.init();
    }

    public static void register() {
        SolRegistries.MOD.apply(ItemHolder.class, (registry, id, instance) -> Registry.register(registry, id, instance.get()));
    }
}
