package net.lcc.sollib.platform;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.SolTest;
import net.lcc.sollib.platform.services.IAccessoryHelper;
import net.lcc.sollib.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

public class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IAccessoryHelper ACCESSORY = load(IAccessoryHelper.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        SolLib.MOD.getLogger().info("Loaded", loadedService, "for service", clazz);
        return loadedService;
    }

    public static <T> T load(Class<T> clazz, Class<? extends T> fallback) {
        try {
            for (T loadedService : ServiceLoader.load(clazz)) {
                SolTest.MOD.getLogger().info(loadedService);
                if (loadedService.getClass() == fallback) continue;

                SolLib.MOD.getLogger().info("Loaded", loadedService, "for service", clazz);
                return loadedService;
            }

            T loadedService = fallback.getConstructor().newInstance();
            SolLib.MOD.getLogger().info("Loaded fallback", loadedService, "for service", clazz);
            return loadedService;
        } catch (Exception e) {
            throw new NullPointerException("Failed to load service for " + clazz.getName());
        }
    }
}