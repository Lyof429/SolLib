package net.lcc.sollib.platform;

import net.lcc.sollib.platform.services.IAccessoryHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Dependency(mod = "curios")
public class ForgeAccessoryHelper implements IAccessoryHelper {
    @Override
    public Map<String, ItemStack> getAccessories(LivingEntity entity) {
        Optional<ICuriosItemHandler> inventory = CuriosApi.getCuriosInventory(entity).resolve();
        if (inventory.isEmpty()) return Map.of();

        Map<String, ItemStack> result = new HashMap<>();
        for (Map.Entry<String, ICurioStacksHandler> entry : inventory.get().getCurios().entrySet())
            result.put(entry.getKey(), entry.getValue().getStacks().getStackInSlot(0));

        return result;
    }
}
