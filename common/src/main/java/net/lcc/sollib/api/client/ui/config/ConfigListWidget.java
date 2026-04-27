package net.lcc.sollib.api.client.ui.config;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.common.config.SolConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConfigListWidget extends AbstractScrollWidget {
    public class ScrollingButton extends Button {
        private final SolConfig config;
        private GuiGraphics guiGraphics;
        
        public ScrollingButton(int x, int y, int width, int height, SolConfig config) {
            super(x, y, width, height, Component.literal(config.getName()),
                    it -> ConfigListWidget.this.selected = config, Button.DEFAULT_NARRATION);
            this.config = config;
        }

        @Override
        public int getY() {
            return super.getY() - (int) ConfigListWidget.this.scrollAmount();
        }

        @Override
        public boolean isFocused() {
            return this.config == ConfigListWidget.this.selected;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            this.guiGraphics = guiGraphics;
            super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        }

        @Override
        public boolean isHoveredOrFocused() {
            if (this.isFocused())
                guiGraphics.setColor(0.7f, 0.7f, 1, this.alpha);
            return super.isHoveredOrFocused();
        }
    }

    private static final int BUTTON_SIZE = 24;

    private final List<AbstractButton> buttons;
    private SolConfig selected;

    public ConfigListWidget(double x, double y, double width, double height, Component message, Iterable<SolConfig> configs) {
        super((int) x, (int) y, (int) width, (int) height, message);

        int offset = (int) y;
        this.buttons = new ArrayList<>();
        for (SolConfig config : configs) {
            this.buttons.add(new ScrollingButton((int) x + 1, offset, (int) width - 2, BUTTON_SIZE, config));
            offset += BUTTON_SIZE;
        }
        this.selected = null;
    }

    @Override
    protected int getInnerHeight() {
        return this.buttons.size() * BUTTON_SIZE;
    }

    @Override
    protected double scrollRate() {
        return 9;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.visible) {
            this.renderBackground(guiGraphics);
            guiGraphics.enableScissor(this.getX() + 1, this.getY() + 1, this.getX() + this.width - 1, this.getY() + this.height - 1);
            this.renderContents(guiGraphics, mouseX, mouseY, partialTick);
            guiGraphics.disableScissor();
            this.renderDecorations(guiGraphics);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean r = super.mouseClicked(mouseX, mouseY, button);
        if (!r) return false;

        for (AbstractButton b : this.buttons) {
            if (b.isHovered()) {
                b.onClick(mouseX, mouseY);
                return true;
            }
        }

        return false;
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        for (AbstractButton button : this.buttons)
            button.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    public SolConfig getSelected() {
        return this.selected;
    }
}
