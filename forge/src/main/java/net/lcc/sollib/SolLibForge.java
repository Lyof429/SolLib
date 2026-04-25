package net.lcc.sollib;

import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.ItemHolder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod(SolLib.MOD_ID)
@Mod.EventBusSubscriber(modid = SolLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SolLibForge {
    public SolLibForge() {
        SolLib.init();
    }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        SolRegistries.MOD.apply(ItemHolder.class, (registry, id, instance) -> event.register(registry.key(), id, instance));
    }
}
