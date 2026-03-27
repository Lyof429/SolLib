package net.lcc.sollib;

import net.fabricmc.api.ModInitializer;

public class SolLibFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SolLib.init();
    }
}
