package net.lcc.sollib.api.client.ui.config;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lcc.sollib.api.common.config.SolConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ConfigButton extends Button {
    private final ConfigListWidget self;
    private final SolConfig config;

    public ConfigButton(ConfigListWidget self, int x, int y, int width, int height, SolConfig config) {
        super(x, y, width, height, Component.literal(config.getName()),
                it -> self.setSelected(config), Button.DEFAULT_NARRATION);

        this.self = self;
        this.config = config;
        if (this.config.isOutdated())
            this.setTooltip(Tooltip.create(Component.literal("Outdated config! Consider resetting it!")));
    }

    @Override
    public int getY() {
        return super.getY() - self.getScrollAmount();
    }

    @Override
    public boolean isFocused() {
        return this.config == self.getSelected();
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        int sx = this.getX(), sy = this.getY(), ex = this.getX() + this.width, ey = this.getY() + this.height;
        if (this.isHovered()) guiGraphics.fill(sx, sy, ex, ey, 0xffffffff);
        int color = 0xff6F6F6F;
        if (this.isFocused()) color = this.getTooltip() == null ? 0xff808BBC : 0xff8B8660;
        guiGraphics.fill(sx + 1, sy + 1, ex - 1, ey - 1, color);
        if (this.getTooltip() != null) {
            guiGraphics.fill(sx + 4, sy + 4, sx + 8, ey - 10, 0xff000000);
            guiGraphics.fill(sx + 4, ey - 8, sx + 8, ey - 4, 0xff000000);
        }

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.active ? 16777215 : 10526880;
        this.renderString(guiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
    }
}
