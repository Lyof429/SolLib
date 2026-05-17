package net.lcc.sollib.api.client;

import net.lcc.sollib.api.client.render.block.SBlockRendererRegistry;
import net.lcc.sollib.api.client.render.item.SItemRendererRegistry;
import net.lcc.sollib.api.client.ui.bossbar.SBossBarRegistry;

public class SolClientRegistries {
    public static class Render {
        public static final SItemRendererRegistry ITEM = new SItemRendererRegistry();
        public static final SBlockRendererRegistry BLOCK = new SBlockRendererRegistry();
    }
    public static final SBossBarRegistry BOSS_BAR = new SBossBarRegistry();
}
