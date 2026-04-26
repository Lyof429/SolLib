package net.lcc.sollib;

import com.terraformersmc.modmenu.ModMenu;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.util.SystemProperties;
import net.lcc.sollib.core.SolFabricCore;

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
        SolFabricCore.register();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
        SolFabricCore.registerClient();
    }
}
