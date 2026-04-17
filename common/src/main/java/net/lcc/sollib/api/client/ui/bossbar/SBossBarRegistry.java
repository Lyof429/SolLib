package net.lcc.sollib.api.client.ui.bossbar;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class SBossBarRegistry {
    private final Map<Predicate<BossEvent>, IBossBarRenderer> INSTANCES = new LinkedHashMap<>();

    /**
     * Manages registration of custom boss bar render introduced in SolLib
     * @param condition Filters the actual boss bar event to be processed by {@link IBossBarRenderer}
     * @param renderer Management of how custom boss bar should be rendered
     * @since 1.0.0
     */
    public void register(Predicate<BossEvent> condition, IBossBarRenderer renderer) {
        INSTANCES.put(condition, renderer);
    }

    /**
     * Variant of {@link #register(Predicate, IBossBarRenderer)} of boss bar render registration with default placement
     * Processes only custom texture to render for boss bar
     * @since 1.0.0
     */
    public void register(Predicate<BossEvent> condition, ResourceLocation texture) {
        INSTANCES.put(condition, ((guiGraphics, x, y, bossEvent) -> {
            guiGraphics.blit(texture, x, y - 2, 0, 0, 183, 9, 183, 9);

            int progressWidth = (int) (bossEvent.getProgress() * 183.0F);
            if (progressWidth > 0) {
                guiGraphics.blit(texture, x, y - 2, 0, 9, progressWidth, 9, 183, 9);
            }
        }));
    }

    /**
    * Processes custom renderers registered by {@link #register(Predicate, IBossBarRenderer)} and used by {@link net.lcc.sollib.mixin.client.BossHealthOverlayMixin}
    * @since 1.0.0
    * */
    public IBossBarRenderer getRenderer(BossEvent bossEvent) {
        for (Map.Entry<Predicate<BossEvent>, IBossBarRenderer> entry : INSTANCES.entrySet()) {
            if (entry.getKey().test(bossEvent)) {
                return entry.getValue();
            }
        }
        return null;
    }
}