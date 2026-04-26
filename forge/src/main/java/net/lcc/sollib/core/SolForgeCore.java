package net.lcc.sollib.core;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.lcc.sollib.event.SAxeStrippableEvent;
import net.lcc.sollib.event.SBlockFlammabilityEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SolLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SolForgeCore {
    @SubscribeEvent
    public static void register(FurnaceFuelBurnTimeEvent event) {
        SolRegistries.MOD.apply(ItemHolder.class, holder -> {
            if (event.getItemStack().is(holder.get()) && holder.isFuel())
                event.setBurnTime(holder.getFuelDuration());
        });
    }

    @SubscribeEvent
    public static void register(SAxeStrippableEvent event) {
        SolRegistries.MOD.apply(BlockHolder.class, holder -> {
            if (event.getBlock() == holder.get() && holder.hasStripResult())
                event.setStrippingResult(holder.getStripResult().get());
        });
    }

    @SubscribeEvent
    public static void register(SBlockFlammabilityEvent event) {
        SolRegistries.MOD.apply(BlockHolder.class, holder -> {
            if (event.getBlock() == holder.get() && holder.isFlammable())
                event.setFlammability(holder.getFlammability());
        });
    }
}
