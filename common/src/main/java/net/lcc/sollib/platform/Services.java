package net.lcc.sollib.platform;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.SolTest;
import net.lcc.sollib.platform.services.IAccessoryHelper;
import net.lcc.sollib.platform.services.IPlatformHelper;

import java.util.Iterator;
import java.util.ServiceLoader;

public class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IAccessoryHelper ACCESSORY = load(IAccessoryHelper.class, IAccessoryHelper.Default.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        SolLib.MOD.getLogger().info("Loaded", loadedService, "for service", clazz);
        return loadedService;
    }

    public static <T> T load(Class<T> clazz, Class<? extends T> fallback) {
        try {
            Iterator<T> iterator = ServiceLoader.load(clazz).iterator();
            while (iterator.hasNext()) {
                try {
                    T loadedService = iterator.next();

                    Dependency dependency = loadedService.getClass().getAnnotation(Dependency.class);
                    if (dependency != null && !Services.PLATFORM.isModLoaded(dependency.mod())) {
                        SolTest.MOD.getLogger().info("Found implementation, but", dependency.mod(), "isn't loaded");
                        continue;
                    }

                    SolLib.MOD.getLogger().info("Loaded", loadedService, "for service", clazz);
                    return loadedService;
                } catch (Exception ignored) {
                    SolTest.MOD.getLogger().info(ignored);
                }
            }

            T loadedService = fallback.getConstructor().newInstance();
            SolLib.MOD.getLogger().info("Loaded fallback", loadedService, "for service", clazz);
            return loadedService;
        } catch (Exception e) {
            throw new NullPointerException("Failed to load service for " + clazz.getName());
        }
    }
}