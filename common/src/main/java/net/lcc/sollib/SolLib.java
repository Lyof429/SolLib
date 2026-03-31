package net.lcc.sollib;

import net.lcc.sollib.api.common.config.Configurable;
import net.lcc.sollib.api.common.config.JsonBuilder;
import net.lcc.sollib.api.common.config.SolConfig;
import net.lcc.sollib.api.common.config.SolConfigRegistry;
import net.lcc.sollib.api.common.logger.SolLogger;

import java.util.function.Consumer;

public class SolLib {
    public static final String MOD_ID = "sollib";
    public static final SolLogger LOG = new SolLogger("SolLib");

    public static void init() {
        SolTest.init();
    }
}