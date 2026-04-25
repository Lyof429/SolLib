package net.lcc.sollib.api.common.registry;

import net.lcc.sollib.api.common.registry.holder.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface IRegistryConsumer<T, H extends Holder<T>> {
    void register(Registry<T> registry, ResourceLocation id, H instance);
}
