package net.lcc.sollib.api.client.ui.config;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lcc.sollib.api.common.config.LoadResult;
import net.lcc.sollib.api.common.config.SolConfig;
import net.lcc.sollib.mixin.access.AbstractScrollWidgetAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ConfigWidget extends AbstractWidget {
    private final ConfigListWidget self;
    private final SolConfig config;

    public ConfigWidget(ConfigListWidget self, int x, int y, int width, int height, SolConfig config) {
        super(x, y, width, height, Component.literal(config.getName()));

        this.self = self;
        this.config = config;

        this.reload();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    public void reload() {
        this.config.init();
        this.setTooltip(this.getLoadResult().message == null
                ? null : Tooltip.create(Component.literal(this.getLoadResult().message)));
    }

    @Override
    public int getY() {
        return super.getY() - (int) ((AbstractScrollWidgetAccessor) self).getScrollAmount();
    }

    public SolConfig getConfig() {
        return this.config;
    }

    protected LoadResult getLoadResult() {
        return this.config.getContent().result;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        int sx = this.getX(), sy = this.getY(), ex = this.getX() + this.width, ey = this.getY() + this.height;
        if (this.isHovered() || this == self.getSelected()) guiGraphics.fill(sx, sy, ex, ey, 0xffffffff);
        guiGraphics.fill(sx + 1, sy + 1, ex - 1, ey - 1, this.getLoadResult().color);

        if (this.getLoadResult().message != null) {
            guiGraphics.fill(ex - 8, sy + 4, ex - 4, ey - 10, 0xff000000);
            guiGraphics.fill(ex - 8, ey - 8, ex - 4, ey - 4, 0xff000000);
        }

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.active ? 16777215 : 10526880;
        this.renderString(guiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    protected void renderString(GuiGraphics guiGraphics, Font font, int color) {
        int minX = this.getX() + 4, minY = this.getY(),
                maxX = this.getX() + this.width - 10, maxY = this.getY() + this.getHeight();
        int j = (minY + maxY - 9) / 2 + 1;

        guiGraphics.enableScissor(minX, minY, maxX, maxY);
        guiGraphics.drawString(font, this.getMessage(), minX, j, color);
        guiGraphics.disableScissor();
    }
}
