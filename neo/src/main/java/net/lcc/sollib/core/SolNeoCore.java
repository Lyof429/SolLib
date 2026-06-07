package net.lcc.sollib.core;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.EffectHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.lcc.sollib.event.SAxeStrippableEvent;
import net.lcc.sollib.event.SBlockFlammabilityEvent;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

@EventBusSubscriber(modid = SolLib.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class SolNeoCore {
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

    @SubscribeEvent
    public static void register(RegisterBrewingRecipesEvent event) {
        SolRegistries.MOD.iterate(EffectHolder.class, holder -> {
            if (holder.hasPotion()) {
                event.getBuilder().addRecipe(new PotionRecipe(holder.getCraftingBase().get(), holder.getCraftingIngredient().get().asItem(), holder.getPotion().get()));

                if (holder.hasLongPotion())
                    event.getBuilder().addRecipe(new PotionRecipe(holder.getPotion().get(), Items.REDSTONE, holder.getLongPotion().get()));
                if (holder.hasStrongPotion())
                    event.getBuilder().addRecipe(new PotionRecipe(holder.getPotion().get(), Items.GLOWSTONE_DUST, holder.getStrongPotion().get()));
            }
        });
    }
}
