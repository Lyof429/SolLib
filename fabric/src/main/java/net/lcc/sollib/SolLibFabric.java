package net.lcc.sollib;

import com.terraformersmc.modmenu.gui.ModMenuOptionsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.impl.util.SystemProperties;
import net.lcc.sollib.core.SolFabricCore;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.server.MinecraftServer;

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
