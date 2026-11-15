package net.lcc.sollib.core;

import net.minecraft.resources.ResourceLocation;

import java.util.function.UnaryOperator;

public class Identifier {
    public static ResourceLocation of(String id) {
        return new ResourceLocation(id);
    }

    public static ResourceLocation of(String namespace, String name) {
        return ResourceLocation.tryBuild(namespace, name);
    }

    public static ResourceLocation mutate(ResourceLocation original, UnaryOperator<String> edit) {
        return of(original.getNamespace(), edit.apply(original.getPath()));
    }
}
