package net.lcc.sollib.api;

import net.lcc.sollib.api.client.render.item.SItemRendererRegistry;
import net.lcc.sollib.api.client.ui.bossbar.SBossBarRegistry;

public class SolClientRegistries {
    public static final SItemRendererRegistry ITEM_RENDERER = new SItemRendererRegistry();
    public static final SBossBarRegistry BOSS_BAR = new SBossBarRegistry();
}
