package net.lcc.sollib.api.client.ui.config;

import net.lcc.sollib.api.common.config.SolConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConfigListWidget extends AbstractScrollWidget {
    public class ScrollingButton extends Button {
        public ScrollingButton(int x, int y, int width, int height, Component message, OnPress onPress) {
            super(x, y, width, height, message, onPress, Button.DEFAULT_NARRATION);
        }

        @Override
        public int getY() {
            return super.getY() - (int) ConfigListWidget.this.scrollAmount();
        }
    }

    private static final int BUTTON_SIZE = 24;

    private final List<AbstractButton> buttons;

    public ConfigListWidget(double x, double y, double width, double height, Component message, Iterable<SolConfig> configs) {
        super((int) x, (int) y, (int) width, (int) height, message);

        int offset = (int) y;
        this.buttons = new ArrayList<>();
        for (SolConfig config : configs) {
            this.buttons.add(new ScrollingButton((int) x + 1, offset, (int) width - 2, BUTTON_SIZE,
                    Component.literal(config.getName()), button -> config.openFile()));
            offset += BUTTON_SIZE;
        }
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
}
