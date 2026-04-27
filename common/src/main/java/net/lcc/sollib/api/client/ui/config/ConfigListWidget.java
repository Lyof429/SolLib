package net.lcc.sollib.api.client.ui.config;

import net.lcc.sollib.api.common.config.SolConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ConfigListWidget extends AbstractScrollWidget {
    @FunctionalInterface
    public interface IAction {
        void call();
    }
    public static final int BUTTON_SIZE = 24;

    private final List<AbstractButton> buttons;
    private SolConfig selected;
    private final IAction onClicked;

    public ConfigListWidget(int x, int y, int width, int height, Component message, Iterable<SolConfig> configs,
                            IAction onClicked) {
        super(x, y, width, height, message);

        y += 1;
        this.buttons = new ArrayList<>();
        for (SolConfig config : configs) {
            this.buttons.add(new ConfigButton(this, x + 1, y, width - 2, BUTTON_SIZE, config));
            y += BUTTON_SIZE;
        }
        this.selected = null;
        this.onClicked = onClicked;
    }

    @Override
    protected double scrollRate() {
        return 9;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    @Override
    protected int getInnerHeight() {
        return this.buttons.size() * BUTTON_SIZE;
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
        for (AbstractButton b : this.buttons) {
            if (b.isHovered()) {
                b.mouseClicked(mouseX, mouseY, button);
                r = true;
                break;
            }
        }

        if (r) this.onClicked.call();
        return true;
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        for (AbstractButton button : this.buttons)
            button.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    public SolConfig getSelected() {
        return this.selected;
    }

    public void setSelected(SolConfig selected) {
        this.selected =  this.selected == selected ? null : selected;
    }

    protected int getScrollAmount() {
        return (int) this.scrollAmount();
    }
}
