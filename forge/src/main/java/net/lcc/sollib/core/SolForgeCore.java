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
        ItemHolder holder = SolRegistries.MOD.find(ItemHolder.class,
                it -> event.getItemStack().is(it.get()) && it.isFuel());
        if (holder != null) event.setBurnTime(holder.getFuelDuration());
    }

    @SubscribeEvent
    public static void register(SAxeStrippableEvent event) {
        BlockHolder holder = SolRegistries.MOD.find(BlockHolder.class,
                it -> event.getBlock() == it.get() && it.hasStripResult());
        if (holder != null) event.setStrippingResult(holder.getStripResult().get());
    }

    @SubscribeEvent
    public static void register(SBlockFlammabilityEvent event) {
        BlockHolder holder = SolRegistries.MOD.find(BlockHolder.class,
                it -> event.getBlock() == it.get() && it.isFlammable());
        if (holder != null) event.setFlammability(holder.getFlammability());
    }
}
