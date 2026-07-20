package net.lcc.sollib.mixin.client.ui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lcc.sollib.SolTest;
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
    @Unique private final List<ColorProvider> textColors = new ArrayList<>();
    @Unique private final List<ColorProvider> textHighlights = new ArrayList<>();

    @Unique private int index;
    @Unique private int displayIndex;
    @Unique private String line;
    @Unique private boolean hadNewLine = true;

    @Override
    public StyledMultiLineEditBox sol_withTextColor(ColorProvider provider) {
        this.textColors.add(provider);
        return this;
    }

    @Override
    public StyledMultiLineEditBox sol_withTextHighlight(ColorProvider provider) {
        this.textHighlights.add(provider);
        return this;
    }

    @Inject(method = "renderContents", at = @At("HEAD"))
    private void beginRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        this.index = 0;
        this.displayIndex = -1;
        this.line = "";
    }

    @WrapOperation(method = "renderContents", at = @At(
            value = "INVOKE",
            target = "Ljava/util/Iterator;next()Ljava/lang/Object;"
    ))
    private <E> E next(Iterator<E> instance, Operation<E> original, GuiGraphics guiGraphics) {
        MultilineTextField.StringView o = (MultilineTextField.StringView) original.call(instance);
        String s = this.textField.value();

        this.displayIndex += 1;
        if (this.hadNewLine) {
            this.index += 1;
            this.line = "";
        }
        this.line += s.substring(o.beginIndex(), o.endIndex()) + "\n";

        for (ColorProvider provider : this.textHighlights) {
            int c = provider.getColor(s.substring(o.beginIndex(), o.endIndex()), this.line, this.index);
            if (c == -1) continue;

            int sy = this.getY() + this.innerPadding();
            guiGraphics.fill(this.getX(), sy + this.font.lineHeight*this.displayIndex,
                    this.getX() + this.width, sy + this.font.lineHeight*(this.displayIndex+1), c + 0xff000000);
            break;
        }

        this.hadNewLine = o.endIndex() >= s.length() || s.charAt(o.endIndex()) == '\n';
        return (E) o;
    }

    @WrapOperation(method = "renderContents", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)I"
    ))
    private int drawTextColor(GuiGraphics instance, Font font, String text, int x, int y, int color, Operation<Integer> original) {
        for (ColorProvider provider : this.textColors) {
            int c = provider.getColor(text, this.line, this.index);
            if (c == -1) continue;

            color = c + 0xff000000;
            break;
        }
        return original.call(instance, font, text, x, y, color);
    }
}
