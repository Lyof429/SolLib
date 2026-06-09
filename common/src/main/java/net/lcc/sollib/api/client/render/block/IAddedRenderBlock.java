package net.lcc.sollib.api.client.render.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface IAddedRenderBlock extends IBlockRenderer {
    boolean shouldRender(BlockState state);
}
