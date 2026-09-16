package net.lcc.sollib.api.client.ui.bossbar;

import net.lcc.sollib.mixin.access.BossHealthOverlayAccessor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.world.BossEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.function.Predicate;

public class SBossBarRegistry {
    public static final SBossBarRegistry INSTANCE = new SBossBarRegistry();

    private SBossBarRegistry() {
    }

    private final Map<Predicate<BossEvent>, IBossBarRenderer> BOSSBAR_RENDERER = new LinkedHashMap<>();
    private final Map<Predicate<BossEvent>, IBossBarTextModifier> TEXT_MODIFIERS = new LinkedHashMap<>();
    private final Map<Predicate<BossEvent>, Music> BOSS_MUSIC = new LinkedHashMap<>();

    /**
     * Manages registration of custom boss bar render introduced in SolLib
     *
     * @param condition Filters the actual boss bar event to be processed by {@link IBossBarRenderer}
     * @param renderer  Management of how custom boss bar should be rendered
     * @since 1.0
     */
    public void register(Predicate<BossEvent> condition, IBossBarRenderer renderer) {
        BOSSBAR_RENDERER.put(condition, renderer);
    }

    /**
     * Redeclaration of vanilla GUI_BARS_LOCATION from BossHealthOverlay
     */
    private static final ResourceLocation GUI_BARS_LOCATION = new ResourceLocation("textures/gui/bars.png");

    /**
     * Variant of {@link #register(Predicate, IBossBarRenderer)} of boss bar render registration with default placement
     * Processes only custom texture to render for boss bar
     *
     * @since 1.0
     */
    public void register(Predicate<BossEvent> condition, ResourceLocation texture) {
        BOSSBAR_RENDERER.put(condition, ((guiGraphics, x, y, bossEvent) -> {
            int progressWidth = (int) (bossEvent.getProgress() * 183.0F);
            int colorVOffset = bossEvent.getColor().ordinal() * 5 * 2;
            guiGraphics.blit(GUI_BARS_LOCATION, x, y, 0, colorVOffset, 182, 5);

            if (progressWidth > 0) {
                guiGraphics.blit(GUI_BARS_LOCATION, x, y, 0, colorVOffset + 5, progressWidth, 5);
            }

            guiGraphics.blit(texture, x, y - 2, 0, 0, 183, 9, 183, 9);
        }));
    }

    /**
     * Processes custom renderers registered by {@link #register(Predicate, IBossBarRenderer)} and used by {@link net.lcc.sollib.mixin.client.BossHealthOverlayMixin}
     *
     * @since 1.0
     *
     */
    @ApiStatus.Internal
    public IBossBarRenderer getRenderer(BossEvent bossEvent) {
        for (Map.Entry<Predicate<BossEvent>, IBossBarRenderer> entry : BOSSBAR_RENDERER.entrySet()) {
            if (entry.getKey().test(bossEvent)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Registers a modifier to change the text displayed above a boss bar.
     *
     * @param condition Filters the actual boss bar event.
     * @param modifier  The modifier logic that supplies the new Component.
     * @since 1.4
     */
    public void registerTextModifier(Predicate<BossEvent> condition, IBossBarTextModifier modifier) {
        TEXT_MODIFIERS.put(condition, modifier);
    }

    /**
     * Processes custom text modifiers registered by {@link #registerTextModifier(Predicate, IBossBarTextModifier)} to alter the rendered boss bar text properties
     *
     * @since 1.4
     */
    @ApiStatus.Internal
    public BossBarTextState getModifiedTextState(BossEvent bossEvent, BossBarTextState initialState, GuiGraphics guiGraphics) {
        BossBarTextState currentState = initialState;
        for (Map.Entry<Predicate<BossEvent>, IBossBarTextModifier> entry : TEXT_MODIFIERS.entrySet()) {
            if (entry.getKey().test(bossEvent)) {
                currentState = entry.getValue().modify(bossEvent, currentState, guiGraphics);
            }
        }
        return currentState;
    }

    /**
     * Registers a music type to be played on active boss event.
     *
     * @param condition Filters the actual boss bar event.
     * @param music     The music type to be played on active boss event.
     * @since 1.4
     */
    public void registerMusic(Predicate<BossEvent> condition, Music music) {
        BOSS_MUSIC.put(condition, music);
    }

    @ApiStatus.Internal
    public Music getCustomBossMusic(BossHealthOverlayAccessor overlay) {
        for (LerpingBossEvent event : overlay.getEvents().values()) {
            Music music = getMusic(event);
            if (music != null) {
                return music;
            }
        }
        return null;
    }

    @ApiStatus.Internal
    public Music getMusic(BossEvent bossEvent) {
        for (Map.Entry<Predicate<BossEvent>, Music> entry : BOSS_MUSIC.entrySet()) {
            if (entry.getKey().test(bossEvent)) {
                return entry.getValue();
            }
        }
        return null;
    }
}