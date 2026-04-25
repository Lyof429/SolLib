package net.lcc.sollib.event;

import net.lcc.sollib.api.common.registry.data.Flammability;
import net.lcc.sollib.mixin.FireBlockMixin;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * {@link BlockFlammabilityEvent} is fired when determining the flammability for a Block. <br>
 * <br>
 * This event is fired from {@link FireBlockMixin}.<br>
 * <br>
 * This event is {@link Cancelable} to prevent later handlers from changing the value.<br>
 * <br>
 * This event does not have a result. {@link HasResult}<br>
 * <br>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
 **/
@Cancelable
public class BlockFlammabilityEvent extends Event {
    @NotNull private final Block block;
    private Flammability result;

    public BlockFlammabilityEvent(@NotNull Block block) {
        this.block = block;
        this.result = null;
    }

    /**
     * Get the Block under examination.
     */
    @NotNull
    public Block getBlock()
    {
        return this.block;
    }

    /**
     * Set the flammability for the given Block.
     */
    public void setFlammability(Flammability result) {
        this.result = result;
        setCanceled(true);
    }

    /**
     * The resulting value of this event, the flammability for the Block.
     */
    @Nullable
    public Flammability getFlammability()
    {
        return this.result;
    }

    public boolean isFlammable() {
        return this.result != null;
    }
}