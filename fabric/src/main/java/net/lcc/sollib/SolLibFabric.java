package net.lcc.sollib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.renderer.entity.PigRenderer;

public class SolLibFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        SolLib.init();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
        SolTest.E.addRenderer(PigRenderer::new);
    }
}
