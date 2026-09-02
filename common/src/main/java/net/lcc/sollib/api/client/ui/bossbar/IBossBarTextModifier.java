package net.lcc.sollib.api.client.ui.bossbar;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.BossEvent;

@FunctionalInterface
public interface IBossBarTextModifier {
    BossBarTextState modify(BossEvent bossEvent, BossBarTextState state, GuiGraphics guiGraphics);
}