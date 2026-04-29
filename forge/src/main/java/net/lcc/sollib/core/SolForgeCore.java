package net.lcc.sollib.core;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.EntityHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.lcc.sollib.event.SAxeStrippableEvent;
import net.lcc.sollib.event.SBlockFlammabilityEvent;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.function.Supplier;

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
