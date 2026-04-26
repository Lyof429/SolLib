package net.lcc.sollib.event;

import net.lcc.sollib.mixin.registry.AxeItemMixin;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * {@link SAxeStrippableEvent} is fired when determining the stripping result for a Block. <br>
 * <br>
 * This event is fired from {@link AxeItemMixin#getSolStrippingState(BlockState, CallbackInfoReturnable)}.<br>
 * <br>
 * This event is {@link Cancelable} to prevent later handlers from changing the value.<br>
 * <br>
 * This event does not have a result. {@link Event.HasResult}<br>
 * <br>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
 **/
@Cancelable
public class SAxeStrippableEvent extends Event {
    @NotNull private final Block block;
    private Block result;

    public SAxeStrippableEvent(@NotNull Block block) {
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
     * Set the stripping result for the given Block.
     */
    public void setStrippingResult(Block result) {
        this.result = result;
        setCanceled(true);
    }

    /**
     * The resulting value of this event, the stripping result for the Block.
     */
    @Nullable
    public Block getStrippingResult()
    {
        return this.result;
    }

    public boolean hasStrippingResult() {
        return this.result != null;
    }
}