package net.lcc.sollib;

import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.gui.ModListScreen;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.registries.RegisterEvent;

@Mod(SolLib.MOD_ID)
@Mod.EventBusSubscriber(modid = SolLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SolLibForge {
    public SolLibForge() {
        SolLib.init();

    }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        SolRegistries.MOD.apply(ItemHolder.class, holder -> event.register(Registries.ITEM, holder.getID(), holder));
        SolRegistries.MOD.apply(BlockHolder.class, holder -> event.register(Registries.BLOCK, holder.getID(), holder));
    }
}
