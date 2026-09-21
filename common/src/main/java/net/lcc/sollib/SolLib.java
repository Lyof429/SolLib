package net.lcc.sollib;

import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.config.SolConfig;
import net.lcc.sollib.api.common.data.reload.IReloadListener;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.api.common.registry.holder.DensityFunctionHolder;
import net.lcc.sollib.api.common.worldgen.density.ProgressionDensityFunction;
import net.lcc.sollib.api.event.SEvents;
import net.minecraft.server.packs.resources.ResourceManager;

public class SolLib {
    public static final String MOD_ID = "sollib";
    public static final SolModContainer MOD = new SolModContainer("SolLib", MOD_ID) {
        @Override
        public Iterable<SolConfig> getConfigs() {
            return SolRegistries.CONFIG.getAll();
        }
    };

    public static void init() {
        SEvents.load();
        MOD.createConfig(MOD_ID, 1.0, builder -> {});

        SolRegistries.Data.RELOAD.register(new IReloadListener() {
            @Override
            public void preload(ResourceManager manager) {
                MOD.getLogger().info("PRELOAD");
                manager.listResources("potion", name -> true).forEach((id, resource) -> {
                    MOD.getLogger().info(id);
                });
            }

            @Override
            public void reload(ResourceManager manager) {}
        });

        MOD.register(DensityFunctionHolder.class, "progression", () -> ProgressionDensityFunction.CODEC);
    }
}