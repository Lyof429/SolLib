package net.lcc.sollib.event;

import net.lcc.sollib.api.common.registry.data.block.Flammability;
import net.lcc.sollib.mixin.registry.FireBlockMixin;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * {@link SBlockFlammabilityEvent} is fired when determining the flammability for a Block. <br>
 * <br>
 * This event is fired from {@link FireBlockMixin}.<br>
 * <br>
 * This event is {@link ICancellableEvent} to prevent later handlers from changing the value.<br>
 * <br>
 * This event does not have a result.<br>
 * <br>
 * This event is fired on the {@link NeoForge#EVENT_BUS}.
 **/
public class SBlockFlammabilityEvent extends Event implements ICancellableEvent {
    @NotNull private final Block block;
    private Flammability result;

    public SBlockFlammabilityEvent(@NotNull Block block) {
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