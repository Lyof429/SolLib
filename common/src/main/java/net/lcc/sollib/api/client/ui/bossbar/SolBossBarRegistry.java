package net.lcc.sollib.api.client.ui.bossbar;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class SolBossBarRegistry {
    private static final Map<Predicate<BossEvent>, SolBossBarRenderer> RENDERERS = new LinkedHashMap<>();

    @FunctionalInterface
    public interface SolBossBarRenderer {
        void render(GuiGraphics guiGraphics, int x, int y, BossEvent bossEvent);
    }

    /**
     * Manages registration of custom boss bar render introduced in SolLib
     * @param condition Filters the actual boss bar event to be processed by {@link SolBossBarRenderer}
     * @param renderer Management of how custom boss bar should be rendered
     * @since 1.0.0
     */
    public static void register(Predicate<BossEvent> condition, SolBossBarRenderer renderer) {
        RENDERERS.put(condition, renderer);
    }

    /**
     * Variant of {@link #register(Predicate, SolBossBarRenderer)} of boss bar render registration with default placement
     * Processes only custom texture to render for boss bar
     * @since 1.0.0
     */
    public static void register(Predicate<BossEvent> condition, ResourceLocation texture) {
        RENDERERS.put(condition, ((guiGraphics, x, y, bossEvent) -> {
            guiGraphics.blit(texture, x, y - 2, 0, 0, 183, 9, 183, 9);

            int progressWidth = (int) (bossEvent.getProgress() * 183.0F);
            if (progressWidth > 0) {
                guiGraphics.blit(texture, x, y - 2, 0, 9, progressWidth, 9, 183, 9);
            }
        }));
    }
    /**
    * Processes custom renderers registered by {@link #register(Predicate, SolBossBarRenderer)} and used by {@link net.lcc.sollib.mixin.client.BossHealthOverlayMixin}
    * @since 1.0.0
    * */
    public static SolBossBarRenderer getRenderer(BossEvent bossEvent) {
        for (Map.Entry<Predicate<BossEvent>, SolBossBarRenderer> entry : RENDERERS.entrySet()) {
            if (entry.getKey().test(bossEvent)) {
                return entry.getValue();
            }
        }
        return null;
    }
}