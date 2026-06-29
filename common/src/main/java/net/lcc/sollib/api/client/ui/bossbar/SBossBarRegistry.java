package net.lcc.sollib.api.client.ui.bossbar;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class SBossBarRegistry {
    public static final SBossBarRegistry INSTANCE = new SBossBarRegistry();

    private SBossBarRegistry() {
    }

    private final Map<Predicate<BossEvent>, IBossBarRenderer> INSTANCES = new LinkedHashMap<>();

    /**
     * Manages registration of custom boss bar render introduced in SolLib
     *
     * @param condition Filters the actual boss bar event to be processed by {@link IBossBarRenderer}
     * @param renderer  Management of how custom boss bar should be rendered
     * @since 1.0
     */
    public void register(Predicate<BossEvent> condition, IBossBarRenderer renderer) {
        INSTANCES.put(condition, renderer);
    }

    /**
     * Redeclaration of vanilla BAR_BACKGROUND_SPRITES and BAR_PROGRESS_SPRITES from BossHealthOverlay
     */
    private static final ResourceLocation[] BAR_BACKGROUND_SPRITES = new ResourceLocation[]{ResourceLocation.withDefaultNamespace("boss_bar/pink_background"), ResourceLocation.withDefaultNamespace("boss_bar/blue_background"), ResourceLocation.withDefaultNamespace("boss_bar/red_background"), ResourceLocation.withDefaultNamespace("boss_bar/green_background"), ResourceLocation.withDefaultNamespace("boss_bar/yellow_background"), ResourceLocation.withDefaultNamespace("boss_bar/purple_background"), ResourceLocation.withDefaultNamespace("boss_bar/white_background")};
    private static final ResourceLocation[] BAR_PROGRESS_SPRITES = new ResourceLocation[]{ResourceLocation.withDefaultNamespace("boss_bar/pink_progress"), ResourceLocation.withDefaultNamespace("boss_bar/blue_progress"), ResourceLocation.withDefaultNamespace("boss_bar/red_progress"), ResourceLocation.withDefaultNamespace("boss_bar/green_progress"), ResourceLocation.withDefaultNamespace("boss_bar/yellow_progress"), ResourceLocation.withDefaultNamespace("boss_bar/purple_progress"), ResourceLocation.withDefaultNamespace("boss_bar/white_progress")};

    /**
     * Variant of {@link #register(Predicate, IBossBarRenderer)} of boss bar render registration with default placement
     * Processes only custom texture to render for boss bar
     *
     * @since 1.0
     */
    public void register(Predicate<BossEvent> condition, ResourceLocation texture) {
        INSTANCES.put(condition, ((guiGraphics, x, y, bossEvent) -> {
            int progress = Mth.lerpDiscrete(bossEvent.getProgress(), 0, 182);
            guiGraphics.blitSprite(BAR_BACKGROUND_SPRITES[bossEvent.getColor().ordinal()], 182, 5, 0, 0, x, y, 182, 5);
            guiGraphics.blitSprite(texture, 183, 9, 0, 0, x, y - 2, 183, 9);
            if (progress > 0)
                guiGraphics.blitSprite(BAR_PROGRESS_SPRITES[bossEvent.getColor().ordinal()], 182, 5, 0, 0, x, y, progress, 5);
        }));
    }

    /**
     * Processes custom renderers registered by {@link #register(Predicate, IBossBarRenderer)} and used by {@link net.lcc.sollib.mixin.client.BossHealthOverlayMixin}
     *
     * @since 1.0
     *
     */
    public IBossBarRenderer getRenderer(BossEvent bossEvent) {
        for (Map.Entry<Predicate<BossEvent>, IBossBarRenderer> entry : INSTANCES.entrySet()) {
            if (entry.getKey().test(bossEvent)) {
                return entry.getValue();
            }
        }
        return null;
    }
}