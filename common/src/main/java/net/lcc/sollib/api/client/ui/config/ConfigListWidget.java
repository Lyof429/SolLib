package net.lcc.sollib.api.client.ui.config;

import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.config.SolConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ConfigListWidget extends AbstractScrollWidget {
    public static final int BUTTON_SIZE = 24;

    private final List<ConfigWidget> widgets;
    private SolConfig selected;

    public ConfigListWidget(int x, int y, int width, int height, Component message, Iterable<SolConfig> configs) {
        super(x, y, width, height, message);

        y += 1;
        this.widgets = new ArrayList<>();
        for (SolConfig config : configs) {
            this.widgets.add(new ConfigWidget(this, x + 1, y, width - 2, BUTTON_SIZE, config));
            y += BUTTON_SIZE;
        }
        this.selected = null;
    }

    @Override
    protected double scrollRate() {
        return 9;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    @Override
    protected int getInnerHeight() {
        return this.widgets.size() * BUTTON_SIZE;
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

        r = false;
        for (ConfigWidget b : this.widgets) {
            if (b.isHovered()) {
                b.mouseClicked(mouseX, mouseY, button);
                r = true;
                break;
            }
        }

        return true;
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        for (ConfigWidget button : this.widgets)
            button.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    public void reload() {
        for (ConfigWidget widget : this.widgets) {
            widget.getConfig().init();
            widget.reload();
        }
    }

    protected int getScrollAmount() {
        return (int) this.scrollAmount();
    }
}
