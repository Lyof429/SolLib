package net.lcc.sollib.api.common.registry.holder;

import net.lcc.sollib.api.common.registry.Holder;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Supplier;

public class EnchantHolder extends Holder<Enchantment> {
    public EnchantHolder(SolModContainer mod, String name, Supplier<Enchantment> entrySupplier) {
        super(mod, name, entrySupplier);
    }

    @Override
    protected Registry<Enchantment> getRegistry() {
        return BuiltInRegistries.ENCHANTMENT;
    }
}
