package net.lcc.sollib.api.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lcc.sollib.api.client.ui.bossbar.SolBossBarRegistry;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class SolBuiltinItemRenderRegistry {
    private static final Map<Predicate<ItemStack>, SolItemRenderer> RENDERERS = new LinkedHashMap<>();

    @FunctionalInterface
    public interface SolItemRenderer {
        void render(ItemStack stack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay, EntityModelSet entityModelSet);
    }

    /**
     * Manages registration of item stack render introduced in SolLib
     * @param condition Filters the actual item stack to be processed by {@link SolItemRenderer}
     * @param renderer  Management of how item stack should be rendered
     * @since 1.0.0
     */
    public static void register(Predicate<ItemStack> condition, SolItemRenderer renderer) {
        RENDERERS.put(condition, renderer);
    }

    /**
     * Processes custom renderers registered by {@link #register(Predicate, SolItemRenderer)} and used by {@link net.lcc.sollib.mixin.client.render.BlockEntityWithoutLevelRendererMixin}
     * @since 1.0.0
     */
    public static SolItemRenderer getRenderer(ItemStack stack) {
        for (Map.Entry<Predicate<ItemStack>, SolItemRenderer> entry : RENDERERS.entrySet()) {
            if (entry.getKey().test(stack)) {
                return entry.getValue();
            }
        }
        return null;
    }
}