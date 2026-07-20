package net.lcc.sollib.api.client.ui.config;

import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.config.SolConfig;
import net.lcc.sollib.api.common.config.builder.JsonBuilder;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class SolConfigScreen extends Screen {
    private final SolModContainer modContainer;
    private final Screen previous;

    private ConfigListWidget configList;
    private MultiLineEditBox editBox;
    private Button saveButton;
    private Button openButton;
    private Button resetButton;
    private Button reloadButton;

    private String lastText;
    private Component fileStatus;
    private Component errorMessage;
    private int errorLine, errorColumn;

    public SolConfigScreen(SolModContainer modContainer, Screen previous) {
        super(Component.literal(modContainer.getName()));
        this.modContainer = modContainer;
        this.previous = previous;

        this.lastText = "";
        this.fileStatus = Component.empty();
        this.errorLine = -1;
        this.errorColumn = -1;
    }

    @Override
    protected void init() {
        super.init();

        this.configList = new ConfigListWidget(20, this.height / 4,
                this.width / 4 - 30, this.height / 2, this.modContainer.getConfigs(), this::onConfigSelected);
        this.addRenderableWidget(this.configList);

        this.editBox = StyledMultiLineEditBox.of(this.font, this.width / 4 + 30, 50, 3 * this.width / 4  - 50,
                this.height - 130, Component.literal("Select a file to edit"), Component.literal("Edit"))
                .sol_withLineIndex(true)
                .sol_withTextHighlight((text, line, index) -> index == this.errorLine ? 0x990000 : -1)
                .sol_withTextColor((text, line, index) -> line.strip().startsWith("//") ? 0x777777 : -1)
                .sol_withTextColor((text, line, index) -> line.startsWith("version:") || line.startsWith("reset:") ? 0xbb7700 : -1).build();
        this.addRenderableWidget(this.editBox);

        int buttonSize = (3 * this.width / 4 - 40 - 30) / 6;

        this.saveButton = Button.builder(Component.translatable("gui.sollib.config.save"), this::onButtonClick)
                .pos(this.width / 4 + 30 + 3*buttonSize, this.height - 80 + 10).size(buttonSize, 20).build();
        this.addRenderableWidget(this.saveButton);
        this.openButton = Button.builder(Component.translatable("gui.sollib.config.open"), this::onButtonClick)
                .pos(this.width / 4 + 30 + 4*buttonSize + 10, this.height - 80 + 10).size(buttonSize, 20).build();
        this.addRenderableWidget(this.openButton);
        this.resetButton = Button.builder(Component.translatable("gui.sollib.config.reset").withStyle(ChatFormatting.DARK_RED), this::onButtonClick)
                .pos(this.width / 4 + 30 + 5*buttonSize + 20, this.height - 80 + 10).size(buttonSize, 20).build();
        this.addRenderableWidget(this.resetButton);

        buttonSize = (this.width / 2 - 20) / 2;
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE,
                        button -> this.onClose())
                .pos(this.width * 3 / 4 - buttonSize, this.height - 27).size(buttonSize, 20).build());
        this.reloadButton = Button.builder(Component.translatable("gui.sollib.config.reload"), this::onButtonClick)
                .pos(this.width / 4, this.height - 27).size(buttonSize, 20).build();
        this.addRenderableWidget(this.reloadButton);

        this.onConfigSelected(null);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderDirtBackground(guiGraphics);
        guiGraphics.setColor(0.15f, 0.15f, 0.15f, 1);
        guiGraphics.blit(BACKGROUND_LOCATION, 0, 35, 0, 0.0F, 0.0F, this.width, this.height - 70, 32, 32);
        guiGraphics.setColor(1, 1, 1, 1);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 5, 16777215);

        if (this.configList.getSelected() != null) {
            guiGraphics.drawString(this.font, this.fileStatus, this.width / 4 + 30, this.height - 80 + 17, 16777215);

            if (this.errorMessage != null
                    && mouseX >= this.width / 4 + 30
                    && mouseY >= this.height - 70
                    && mouseX <= this.width / 4 + 30 + this.font.width(this.fileStatus)
                    && mouseY <= this.height - 70 + this.font.lineHeight*2
            )
                this.setTooltipForNextRenderPass(this.errorMessage);
        }

        //guiGraphics.drawString(this.font, "the what", 10, 10, 16777215);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void tick() {
        super.tick();
        this.editBox.tick();

        if (this.editBox != null && !this.editBox.getValue().equals(this.lastText)) {
            this.lastText = this.editBox.getValue();
            this.errorMessage = null;
            this.errorLine = -1;
            this.errorColumn = -1;

            SolConfig.Content result = new SolConfig.Content();
            result.text = this.lastText;
            String json = SolConfig.toJson(result);
            try {
                JsonBuilder.toJson(json);
                this.fileStatus = Component.literal("JSON Status: Valid").withStyle(ChatFormatting.GREEN);
            } catch (Exception e) {
                String msg = e.getMessage();
                if (msg.contains("at line")) {
                    int atIdx = msg.indexOf("at line");
                    this.errorMessage = Component.literal(msg);

                    msg = msg.substring(atIdx);

                    this.errorLine = Integer.parseInt(msg.substring(msg.indexOf("at line") + 7, msg.indexOf("column")).strip());
                    this.errorColumn = Integer.parseInt(msg.substring(msg.indexOf("column") + 6, msg.indexOf("path")).strip()) - 2;
                }
                this.fileStatus = Component.literal("JSON Error: line " + this.errorLine + ", column " + this.errorColumn).withStyle(ChatFormatting.RED);
            }
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        SolRegistries.CONFIG.reload();
        this.minecraft.setScreen(this.previous);
    }

    public void onConfigSelected(SolConfig config) {
        if (config != null) {
            this.editBox.setValue(config.getContent().text);
        }

        if (this.configList != null) {
            this.saveButton.active = config != null;
            this.openButton.active = config != null;
            this.resetButton.active = config != null;
            this.editBox.active = config != null;
        }
    }

    public void onButtonClick(Button button) {
        if (this.configList == null || this.configList.getSelected() == null) return;

        if (button == this.saveButton) {
            this.configList.getSelected().getConfig().writeFile(this.editBox.getValue());
            this.configList.getSelected().reload();
        }

        else if (button == this.openButton)
            this.configList.getSelected().getConfig().openFile();

        else if (button == this.resetButton) {
            this.configList.getSelected().getConfig().init(true);
            this.configList.getSelected().reload();
            this.onConfigSelected(this.configList.getSelected().getConfig());
        }

        else if (button == this.reloadButton) {
            this.configList.reload();
            this.onConfigSelected(this.configList.getSelected().getConfig());
        }
    }
}
