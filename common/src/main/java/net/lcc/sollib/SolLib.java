package net.lcc.sollib;

import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.api.common.registry.holder.DensityFunctionHolder;
import net.lcc.sollib.api.common.worldgen.density.ProgressionDensityFunction;

public class SolLib {
    public static final String MOD_ID = "sollib";
    public static final SolModContainer MOD = new SolModContainer("SolLib", MOD_ID);

    public static void init() {
        MOD.register(DensityFunctionHolder.class, "progression", () -> ProgressionDensityFunction.CODEC);

        SolTest.lyof();
    }
}