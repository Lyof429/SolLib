package net.lcc.sollib.api.client;

import net.lcc.sollib.api.client.model.item.SItemModelRegistry;
import net.lcc.sollib.api.client.render.block.SBlockRendererRegistry;
import net.lcc.sollib.api.client.render.item.SItemRendererRegistry;
import net.lcc.sollib.api.client.ui.bossbar.SBossBarRegistry;

public class SolClientRegistries {
    public static final SItemModelRegistry ITEM_MODEL = SItemModelRegistry.INSTANCE;
    public static class Render {
        public static final SItemRendererRegistry ITEM = SItemRendererRegistry.INSTANCE;
        public static final SBlockRendererRegistry BLOCK = SBlockRendererRegistry.INSTANCE;
    }
    public static final SBossBarRegistry BOSS_BAR = SBossBarRegistry.INSTANCE;
}
