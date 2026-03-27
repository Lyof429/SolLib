package net.lcc.sollib;

import net.lcc.sollib.logger.SolLogger;
import net.lcc.sollib.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolLib {
    public static final String MOD_ID = "sollib";
    public static final SolLogger LOG = new SolLogger("SolLib");

    public static void init() {
        LOG.info("Hello", Services.PLATFORM.getPlatformName(), "World!");
    }
}