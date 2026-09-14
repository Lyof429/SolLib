package net.lcc.sollib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;

public class SolLibFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        SolLib.init();

        //ResourceConditions.register(LoadCondition.CONFIG, LoadCondition::configMatches);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
    }
}
