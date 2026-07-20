package net.lcc.sollib.api.client.ui.config;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.network.chat.Component;

public interface StyledMultiLineEditBox {
    static StyledMultiLineEditBox of(Font font, int x, int y, int width, int height, Component placeholder, Component message) {
        return (StyledMultiLineEditBox) new MultiLineEditBox(font, x, y, width, height, placeholder, message);
    }

    StyledMultiLineEditBox sol_withTextColor(ColorProvider provider);
    StyledMultiLineEditBox sol_withTextHighlight(ColorProvider provider);

    default MultiLineEditBox build() {
        return (MultiLineEditBox) this;
    }

    @FunctionalInterface
    interface ColorProvider {
        // Returns the color code to be used for this text. Return -1 to pass
        int getColor(String text, String line, int index);
    }
}
