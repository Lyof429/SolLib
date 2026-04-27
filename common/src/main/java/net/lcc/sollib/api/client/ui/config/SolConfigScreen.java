package net.lcc.sollib.api.client.ui.config;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.config.SolConfig;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.platform.Services;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SolConfigScreen extends Screen {
    private SolModContainer modContainer;
    private Screen previous;

    public SolConfigScreen(SolModContainer modContainer, Screen previous) {
        super(Component.literal(modContainer.getName()));
        this.modContainer = modContainer;
        this.previous = previous;
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(new ConfigListWidget(this.width * 0.25, this.height * 0.25,
                this.width * 0.5, this.height * 0.25,
                Component.literal("Configs"), this.modContainer.getConfigs()));
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> this.onClose())
                .pos(this.width / 2 - 100, this.height - 27).size(200, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderDirtBackground(guiGraphics);
        guiGraphics.setColor(0.15f, 0.15f, 0.15f, 1);
        guiGraphics.blit(BACKGROUND_LOCATION, 0, 35, 0, 0.0F, 0.0F, this.width, this.height - 70, 32, 32);
        guiGraphics.setColor(1, 1, 1, 1);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 5, 16777215);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        super.onClose();
        SolRegistries.CONFIG.reload();
        this.minecraft.setScreen(this.previous);
    }
}
