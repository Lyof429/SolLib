package net.lcc.sollib;

import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.api.common.registry.holder.DensityFunctionHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.lcc.sollib.api.common.worldgen.density.ProgressionDensityFunction;
import net.lcc.sollib.core.DebugItem;
import net.lcc.sollib.platform.Services;
import net.minecraft.world.item.Item;

public class SolLib {
    public static final String MOD_ID = "sollib";
    public static final SolModContainer MOD = new SolModContainer("SolLib", MOD_ID);

    public static void init() {
        if (Services.PLATFORM.isDevelopmentEnvironment()) {
            MOD.getLogger().info("Running tests for Lyof");
            SolTest.lyof();

            MOD.getLogger().info("Running tests for Sasha");
            SolTest.sasha();
        }

        MOD.register(DensityFunctionHolder.class, "progression", () -> ProgressionDensityFunction.CODEC);
    }
}