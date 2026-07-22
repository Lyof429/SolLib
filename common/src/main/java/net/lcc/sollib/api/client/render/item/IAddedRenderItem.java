package net.lcc.sollib.api.client.render.item;

import net.minecraft.world.item.ItemStack;

public interface IAddedRenderItem extends IItemRenderer {
    float PX_UNIT = 1/16f;

    boolean shouldAddRender(ItemStack stack);
}
