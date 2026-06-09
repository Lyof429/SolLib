package net.lcc.sollib.api.client.render.block;

import net.minecraft.world.level.block.state.BlockState;

public interface IAddedRenderBlock extends IBlockRenderer {
    boolean shouldAddRender(BlockState state);
}
