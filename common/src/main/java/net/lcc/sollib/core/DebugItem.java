package net.lcc.sollib.core;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.SolTest;
import net.lcc.sollib.platform.Services;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DebugItem extends Item {
    public DebugItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        SolTest.MOD.getLogger().info(Services.ACCESSORY.getAccessories(player));
        return super.use(level, player, usedHand);
    }
}
