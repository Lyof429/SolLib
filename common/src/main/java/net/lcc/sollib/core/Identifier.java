package net.lcc.sollib.core;

import net.minecraft.resources.ResourceLocation;

public class Identifier {
    public static ResourceLocation of(String id) {
        return ResourceLocation.parse(id);
    }

    public static ResourceLocation of(String namespace, String name) {
        return ResourceLocation.tryBuild(namespace, name);
    }
}
