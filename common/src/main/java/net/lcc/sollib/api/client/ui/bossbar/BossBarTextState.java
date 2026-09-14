package net.lcc.sollib.api.client.ui.bossbar;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public record BossBarTextState(Font font, Component text, GuiGraphics guiGraphics, int x, int y, int color, boolean dropShadow, float scale) {
    public BossBarTextState withComponent(Component newText) {
        return new BossBarTextState(this.font, newText, this.guiGraphics, this.x, this.y, this.color, this.dropShadow, this.scale);
    }

    public BossBarTextState withPosition(int newX, int newY) {
        return new BossBarTextState(this.font, this.text, this.guiGraphics, newX, newY, this.color, this.dropShadow, this.scale);
    }

    public BossBarTextState withFont(Font newFont) {
        return new BossBarTextState(newFont, this.text, this.guiGraphics, this.x, this.y, this.color, this.dropShadow, this.scale);
    }

    public BossBarTextState withColor(int newColor) {
        return new BossBarTextState(this.font, this.text, this.guiGraphics, this.x, this.y, newColor, this.dropShadow, this.scale);
    }

    public BossBarTextState withShadow(boolean newDropShadow) {
        return new BossBarTextState(this.font, this.text, this.guiGraphics, this.x, this.y, this.color, newDropShadow, this.scale);
    }

    public BossBarTextState withScale(float newScale) {
        return new BossBarTextState(this.font, this.text, this.guiGraphics, this.x, this.y, this.color, this.dropShadow, newScale);
    }
}