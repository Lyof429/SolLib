package net.lcc.sollib.api.client.ui.config;

import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class SolConfigScreen extends Screen {
    private final SolModContainer modContainer;
    private final Screen previous;

    private ConfigListWidget configList;

    public SolConfigScreen(SolModContainer modContainer, Screen previous) {
        super(Component.literal(modContainer.getName()));
        this.modContainer = modContainer;
        this.previous = previous;
    }

    @Override
    protected void init() {
        super.init();

        this.configList = this.addRenderableWidget(new ConfigListWidget(this.width / 4, this.height / 4,
                this.width / 2, this.height / 2, this.modContainer.getConfigs()));

        int buttonSize = (this.width / 2 - 20) / 2;
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE,
                        button -> this.onClose())
                .pos(this.width / 4, this.height - 27).size(buttonSize, 20).build());
        this.addRenderableWidget(Button.builder(Component.translatable("gui.sollib.config.reload"),
                        button -> this.configList.reload())
                .pos(this.width * 3 / 4 - buttonSize, this.height - 27).size(buttonSize, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderMenuBackground(guiGraphics);

        guiGraphics.setColor(0.15f, 0.15f, 0.15f, 1);
        guiGraphics.blit(MENU_BACKGROUND, 0, 35, 0, 0.0F, 0.0F, this.width, this.height - 70, 32, 32);
        guiGraphics.setColor(1, 1, 1, 1);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 5, 16777215);
    }

    @Override
    public void onClose() {
        super.onClose();
        SolRegistries.CONFIG.reload();
        this.minecraft.setScreen(this.previous);
    }
}
