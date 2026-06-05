package net.lcc.sollib.platform.services;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public interface IAccessoryHelper {
    Map<String, ItemStack> getAccessories(LivingEntity entity);
/*
    class Default implements IAccessoryHelper {
        @Override
        public Map<String, ItemStack> getAccessories(LivingEntity entity) {
            return Map.of();
        }
    }*/
}
