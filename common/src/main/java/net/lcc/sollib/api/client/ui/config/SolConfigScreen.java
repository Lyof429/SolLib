package net.lcc.sollib.api.client.ui.config;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.config.SolConfig;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.platform.Services;
import net.minecraft.ChatFormatting;
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
    private final SolModContainer modContainer;
    private final Screen previous;

    private ConfigListWidget configList;
    private Button edit;
    private Button reset;

    public SolConfigScreen(SolModContainer modContainer, Screen previous) {
        super(Component.literal(modContainer.getName()));
        this.modContainer = modContainer;
        this.previous = previous;
    }

    @Override
    protected void init() {
        super.init();

        this.configList = this.addRenderableWidget(new ConfigListWidget(this.width / 4, this.height / 4,
                this.width / 2, this.height / 2,
                Component.literal("Configs"), this.modContainer.getConfigs(), this::updateSelected));

        int buttonSize = (this.width / 2 - 20) / 3;
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE,
                        button -> this.onClose())
                .pos(this.width / 4, this.height - 27).size(buttonSize, 20).build());
        this.edit = this.addRenderableWidget(Button.builder(Component.literal("Edit"),
                        this::editSelected)
                .pos(this.width / 4 + buttonSize + 10, this.height - 27).size(buttonSize, 20).build());
        this.reset = this.addRenderableWidget(Button.builder(Component.literal("Reset").withStyle(ChatFormatting.DARK_RED),
                        this::resetSelected)
                .pos(3*this.width / 4 - buttonSize, this.height - 27).size(buttonSize, 20).build());
        this.edit.active = false;
        this.reset.active = false;
    }

    protected void updateSelected() {
        this.edit.active = this.configList.getSelected() != null;
        this.reset.active = this.configList.getSelected() != null;
    }

    protected void editSelected(Button button) {
        SolConfig config = this.configList.getSelected();
        if (config != null) config.openFile();
    }

    protected void resetSelected(Button button) {
        SolConfig config = this.configList.getSelected();
        if (config != null) config.init(true);
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
