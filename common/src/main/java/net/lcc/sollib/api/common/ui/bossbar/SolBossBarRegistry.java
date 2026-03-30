package net.lcc.sollib.api.common.ui.bossbar;

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

    public static void register(Predicate<BossEvent> condition, SolBossBarRenderer renderer) {
        RENDERERS.put(condition, renderer);
    }

    public static void register(Predicate<BossEvent> condition, ResourceLocation texture) {
        RENDERERS.put(condition, ((guiGraphics, x, y, bossEvent) -> {
            guiGraphics.blit(texture, x, y - 2, 0, 0, 183, 9, 183, 9);

            int progressWidth = (int) (bossEvent.getProgress() * 183.0F);
            if (progressWidth > 0) {
                guiGraphics.blit(texture, x, y - 2, 0, 9, progressWidth, 9, 183, 9);
            }
        }));
    }

    public static SolBossBarRenderer getRenderer(BossEvent bossEvent) {
        for (Map.Entry<Predicate<BossEvent>, SolBossBarRenderer> entry : RENDERERS.entrySet()) {
            if (entry.getKey().test(bossEvent)) {
                return entry.getValue();
            }
        }
        return null;
    }
}