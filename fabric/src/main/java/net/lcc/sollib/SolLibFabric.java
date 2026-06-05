package net.lcc.sollib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.lcc.sollib.api.common.data.runtime.condition.LoadCondition;

public class SolLibFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        SolLib.init();

        ResourceConditions.register(LoadCondition.CONFIG, LoadCondition::configMatches);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
    }
}
