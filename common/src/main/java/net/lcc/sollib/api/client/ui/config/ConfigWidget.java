package net.lcc.sollib.api.client.ui.config;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lcc.sollib.api.common.config.LoadType;
import net.lcc.sollib.api.common.config.SolConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ConfigWidget extends AbstractWidget {
    private final ConfigListWidget self;
    private final SolConfig config;
    private LoadType loadResult;

    private Button edit;
    private Button reset;

    public ConfigWidget(ConfigListWidget self, int x, int y, int width, int height, SolConfig config) {
        super(x, y, width, height, Component.literal(config.getName()));

        this.self = self;
        this.config = config;

        this.reload();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    public void reload() {
        this.loadResult = this.config.getLoadResult();

        this.setTooltip(this.loadResult.message == null
                ? null : Tooltip.create(Component.literal(this.loadResult.message)));

        int buttonSize = (this.width / 2 - 12 - 4) / 2;
        this.edit = new ScrollingButton(this.getX() + this.width / 2 + 8, super.getY() + 4,
                buttonSize, this.getHeight() - 8,
                Component.literal("Edit"),
                button -> this.config.openFile());
        this.reset = new ScrollingButton(this.getX() + this.width - buttonSize - 4, super.getY() + 4,
                buttonSize, this.getHeight() - 8,
                Component.literal("Reset").withStyle(ChatFormatting.DARK_RED),
                button -> this.config.init(true));
    }

    @Override
    public int getY() {
        return super.getY() - self.getScrollAmount();
    }

    protected SolConfig getConfig() {
        return this.config;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        int sx = this.getX(), sy = this.getY(), ex = this.getX() + this.width, ey = this.getY() + this.height;
        if (this.isHovered()) guiGraphics.fill(sx, sy, ex, ey, 0xffffffff);
        guiGraphics.fill(sx + 1, sy + 1, ex - 1, ey - 1, this.loadResult.color);

        if (this.loadResult.message != null) {
            guiGraphics.fill(sx + this.width / 2, sy + 4, sx + this.width / 2 + 4, ey - 10, 0xff000000);
            guiGraphics.fill(sx + this.width / 2, ey - 8, sx + this.width / 2 + 4, ey - 4, 0xff000000);
        }

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.active ? 16777215 : 10526880;
        this.renderString(guiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);

        this.edit.render(guiGraphics, mouseX, mouseY, partialTick);
        this.reset.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    protected void renderString(GuiGraphics guiGraphics, Font font, int color) {
        int minX = this.getX() + 4, minY = this.getY(),
                maxX = this.getX() + this.width / 2 - 2, maxY = this.getY() + this.getHeight();
        int j = (minY + maxY - 9) / 2 + 1;

        guiGraphics.enableScissor(minX, minY, maxX, maxY);
        guiGraphics.drawString(font, this.getMessage(), minX, j, color);
        guiGraphics.disableScissor();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean r = super.mouseClicked(mouseX, mouseY, button);
        if (!r) return false;

        if (this.edit.isHovered())
            this.edit.mouseClicked(mouseX, mouseY, button);
        else if (this.reset.isHovered())
            this.reset.mouseClicked(mouseX, mouseY, button);

        return true;
    }


    public class ScrollingButton extends Button {
        public ScrollingButton(int x, int y, int width, int height, Component message, OnPress onPress) {
            super(x, y, width, height, message, onPress, Button.DEFAULT_NARRATION);
        }

        @Override
        public int getY() {
            return super.getY() - self.getScrollAmount();
        }
    }
}
