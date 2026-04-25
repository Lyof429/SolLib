package net.lcc.sollib.api.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@FunctionalInterface
public interface IRegistryConsumer<T> {
    void register(Registry<T> registry, ResourceLocation id, Supplier<T> instance);
}
