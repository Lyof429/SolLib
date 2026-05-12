package net.lcc.sollib.core;

import net.minecraft.resources.ResourceLocation;

public class Identifier {
    public static ResourceLocation of(String id) {
        return new ResourceLocation(id);
    }

    public static ResourceLocation of(String namespace, String name) {
        return ResourceLocation.tryBuild(namespace, name);
    }
}
