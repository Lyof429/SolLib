package net.lcc.sollib.api.client.ui.bossbar;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.BossEvent;

@FunctionalInterface
public interface IBossBarRenderer {
    void render(GuiGraphics guiGraphics, int x, int y, BossEvent bossEvent);
}
