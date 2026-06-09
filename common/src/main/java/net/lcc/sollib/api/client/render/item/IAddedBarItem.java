package net.lcc.sollib.api.client.render.item;

import net.minecraft.world.item.ItemStack;

public interface IAddedBarItem {
    boolean shouldAddBarRender(ItemStack stack);
    float getAddedBarFullness(ItemStack stack);
    int getAddedBarColor(ItemStack stack);
}
