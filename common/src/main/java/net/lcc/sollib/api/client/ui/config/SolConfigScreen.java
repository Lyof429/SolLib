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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SolConfigScreen extends Screen {
    private SolModContainer modContainer;
    private Screen previous;

    private final List<Button> buttons;

    public SolConfigScreen(SolModContainer modContainer, Screen previous) {
        super(Component.literal(modContainer.getName()));
        this.modContainer = modContainer;
        this.previous = previous;

        this.buttons = new ArrayList<>();
    }

    public static void openFile(SolConfig config) {
        try {
            Path path = Services.PLATFORM.getConfigDirectory();
            for (String dir : config.getSuffixName().split("/"))
                path = path.resolve(dir);

            File file = path.toFile();

            Util.getPlatform().openFile(file);

        } catch (Exception e) {
            SolLib.LOG.error(config.getName(), ": Error while opening config file\n", e);
        }
    }

    @Override
    protected void init() {
        super.init();

        int y = 0;
        for (Map.Entry<String, SolConfig> entry : this.modContainer.getConfigs().entrySet()) {
            this.buttons.add(this.addWidget(new PlainTextButton(0, y, 16, 64, Component.literal(entry.getKey()),
                    button -> openFile(entry.getValue()), this.minecraft.font)));
            y += 24;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        this.renderDirtBackground(guiGraphics);
        for (Button button : this.buttons)
            button.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        super.onClose();
        SolRegistries.CONFIG.reload();
        this.minecraft.setScreen(this.previous);
    }
}
