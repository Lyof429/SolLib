package net.lcc.sollib.api.client.render.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3fc;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ISodiumBlockRenderer {
    /**
     * @param blockRenderer represents Sodium's BlockRenderer that should be cast on use
     * @param level represents WorldSlice that should be cast on use
     * @param buffers represents ChunkBuildBuffers that should be cast on use
     */
    void render(Object blockRenderer, Object level, BlockState state, BlockPos pos,
                Vector3fc origin, Object buffers, long seed);
}