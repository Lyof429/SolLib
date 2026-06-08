package net.lcc.sollib.mixin.client.model.item;

import net.lcc.sollib.api.client.SolClientRegistries;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Shadow @Final private ItemModelShaper itemModelShaper;

    @ModifyVariable(method = "render", at = @At(value = "HEAD"), argsOnly = true)
    private BakedModel changeHandModel(BakedModel model,ItemStack stack, ItemDisplayContext displayContext) {
        if (displayContext != ItemDisplayContext.GUI && displayContext != ItemDisplayContext.GROUND && displayContext != ItemDisplayContext.FIXED) {
            ModelResourceLocation id = SolClientRegistries.ITEM_MODEL.getModel(stack);
            if (id != null)
                return this.itemModelShaper.getModelManager().getModel(id);
        }
        return model;
    }
}
