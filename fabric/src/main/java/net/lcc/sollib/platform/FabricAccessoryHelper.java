package net.lcc.sollib.platform;

import dev.emi.trinkets.api.*;
import net.lcc.sollib.SolLib;
import net.lcc.sollib.SolTest;
import net.lcc.sollib.platform.services.IAccessoryHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FabricAccessoryHelper implements IAccessoryHelper {
    @Override
    public Map<String, ItemStack> getAccessories(LivingEntity entity) {
        SolTest.MOD.getLogger().info("Retrieving Trinkets for " + entity);

        Optional<TrinketComponent> inventory = TrinketsApi.getTrinketComponent(entity);
        if (inventory.isEmpty()) return Map.of();

        Map<String, ItemStack> result = new HashMap<>();
        for (Map.Entry<String, Map<String, TrinketInventory>> entry : inventory.get().getInventory().entrySet()) {
            for (Map.Entry<String, TrinketInventory> slot : entry.getValue().entrySet()) {
                result.put(entry.getKey() + "/" + slot, slot.getValue().getItem(0));
            }
        }

        return result;
    }
}
