package net.lcc.sollib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.impl.util.SystemProperties;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;

public class SolLibFabric implements ModInitializer, ClientModInitializer, PreLaunchEntrypoint {
    // Ensures SolLib is always loaded last on Fabric, so registry can happen after it's been populated
    @Override
    public void onPreLaunch() {
        String lateLoad = System.getProperty(SystemProperties.DEBUG_LOAD_LATE, "");
        lateLoad = lateLoad.isEmpty() ? SolLib.MOD_ID : lateLoad + "," + SolLib.MOD_ID;
        System.setProperty(SystemProperties.DEBUG_LOAD_LATE, lateLoad);
    }

    @Override
    public void onInitialize() {
        SolLib.init();
        SolLibFabricHandlers.register();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
        SolLibFabricHandlers.registerClient();
    }
}
