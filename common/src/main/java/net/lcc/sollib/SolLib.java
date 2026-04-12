package net.lcc.sollib;

import net.lcc.sollib.api.common.logger.SolLogger;

public class SolLib {
    public static final String MOD_ID = "sollib";
    public static final SolLogger LOG = new SolLogger("SolLib");

    public static void init() {
        SolTest.lyof();
    }
}