package net.lcc.sollib;

import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.lcc.sollib.event.AxeStrippableEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SolLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SolLibForgeHandlers {
    @SubscribeEvent
    public static void register(FurnaceFuelBurnTimeEvent event) {
        SolRegistries.MOD.apply(ItemHolder.class, (registry, id, instance) -> {
            if (event.getItemStack().is(instance.get()) && instance.isFuel())
                event.setBurnTime(instance.getFuelDuration());
        });
    }

    @SubscribeEvent
    public static void register(AxeStrippableEvent event) {
        SolRegistries.MOD.apply(BlockHolder.class, (registry, id, instance) -> {
            if (event.getBlock() == instance.get() && instance.hasStripResult())
                event.setStrippingResult(instance.getStripResult().get());
        });
    }
}
