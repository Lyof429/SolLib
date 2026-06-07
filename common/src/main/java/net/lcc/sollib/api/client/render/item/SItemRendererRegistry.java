package net.lcc.sollib.api.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class SItemRendererRegistry {
    private final Map<Predicate<ItemStack>, IItemRenderer> INSTANCES = new LinkedHashMap<>();

    /**
     * Manages registration of item stack renderers introduced in SolLib <br/>
     * Automatically called on Item subclasses that implement {@link IItemRenderer}
     * @param condition Filters the actual item stack to be processed by {@link IItemRenderer}
     * @param renderer  Management of how item stack should be rendered
     * @since 1.0.0
     */
    public void register(Predicate<ItemStack> condition, IItemRenderer renderer) {
        INSTANCES.put(condition, renderer);
    }

    /**
     * Processes custom renderers registered by {@link #register(Predicate, IItemRenderer)}
     * @since 1.0.0
     */
    @ApiStatus.Internal
    public void apply(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer,
                             int packedLight, int packedOverlay) {
        if (stack == null) return;

        for (Map.Entry<Predicate<ItemStack>, IItemRenderer> entry : INSTANCES.entrySet()) {
            if (!entry.getKey().test(stack)) continue;

            poseStack.pushPose();
            entry.getValue().render(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
            poseStack.popPose();
        }
    }
}