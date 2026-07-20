package net.lcc.sollib.mixin.client.ui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lcc.sollib.api.client.ui.config.StyledMultiLineEditBox;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.MultilineTextField;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(MultiLineEditBox.class)
public abstract class MultiLineEditBoxMixin extends AbstractScrollWidget implements StyledMultiLineEditBox {
    public MultiLineEditBoxMixin(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Shadow @Final private MultilineTextField textField;
    @Shadow @Final private Font font;

    @Unique private final List<ColorProvider> sol_textColors = new ArrayList<>();
    @Unique private final List<ColorProvider> sol_textHighlights = new ArrayList<>();
    @Unique private boolean sol_shouldShowLines = false;

    @Unique private int sol_index;
    @Unique private int sol_displayIndex;
    @Unique private String sol_line;
    @Unique private boolean sol_hadNewLine = true;

    @Override
    public StyledMultiLineEditBox sol_withTextColor(ColorProvider provider) {
        this.sol_textColors.add(provider);
        return this;
    }

    @Override
    public StyledMultiLineEditBox sol_withTextHighlight(ColorProvider provider) {
        this.sol_textHighlights.add(provider);
        return this;
    }

    @Override
    public StyledMultiLineEditBox sol_withLineIndex(boolean enable) {
        this.sol_shouldShowLines = enable;
        return this;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.visible && this.sol_shouldShowLines) {
            this.renderBackground(guiGraphics);
            guiGraphics.enableScissor(this.getX() - 50, this.getY() + 1, this.getX() + this.width - 1, this.getY() + this.height - 1);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0, -this.scrollAmount(), 0.0);
            this.renderContents(guiGraphics, mouseX, mouseY, partialTick);
            guiGraphics.pose().popPose();
            guiGraphics.disableScissor();
            this.renderDecorations(guiGraphics);
        } else super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Inject(method = "renderContents", at = @At("HEAD"))
    private void beginRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        this.sol_index = 0;
        this.sol_displayIndex = -1;
        this.sol_line = "";
    }

    @WrapOperation(method = "renderContents", at = @At(
            value = "INVOKE",
            target = "Ljava/util/Iterator;next()Ljava/lang/Object;"
    ))
    private <E> E next(Iterator<E> instance, Operation<E> original, GuiGraphics guiGraphics) {
        MultilineTextField.StringView o = (MultilineTextField.StringView) original.call(instance);
        String s = this.textField.value();

        this.sol_displayIndex += 1;
        if (this.sol_hadNewLine) {
            this.sol_index += 1;
            this.sol_line = "";
        }
        this.sol_line += s.substring(o.beginIndex(), o.endIndex()) + "\n";

        int sy = this.getY() + this.innerPadding();
        for (ColorProvider provider : this.sol_textHighlights) {
            int c = provider.getColor(s.substring(o.beginIndex(), o.endIndex()), this.sol_line, this.sol_index);
            if (c == -1) continue;

            guiGraphics.fill(this.getX() + 1, sy + this.font.lineHeight*this.sol_displayIndex,
                    this.getX() + this.width, sy + this.font.lineHeight*(this.sol_displayIndex+1), c + 0xff000000);
            break;
        }

        if (this.sol_hadNewLine && this.sol_shouldShowLines)
            guiGraphics.drawString(this.font, String.valueOf(this.sol_index),
                    this.getX() - 4 - this.font.width(String.valueOf(this.sol_index)),
                    sy + this.font.lineHeight*this.sol_displayIndex, 0xff999999);

        this.sol_hadNewLine = o.endIndex() >= s.length() || s.charAt(o.endIndex()) == '\n';
        return (E) o;
    }

    @WrapOperation(method = "renderContents", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)I"
    ))
    private int drawTextColor(GuiGraphics instance, Font font, String text, int x, int y, int color, Operation<Integer> original) {
        for (ColorProvider provider : this.sol_textColors) {
            int c = provider.getColor(text, this.sol_line, this.sol_index);
            if (c == -1) continue;

            color = c + 0xff000000;
            break;
        }
        return original.call(instance, font, text, x, y, color);
    }
}
