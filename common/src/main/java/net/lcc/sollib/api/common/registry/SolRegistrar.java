package net.lcc.sollib.api.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;

public class SolRegistrar {
    private final String modid;

    public SolRegistrar(String modid) {
        this.modid = modid;
    }

    public String getModID() {
        return this.modid;
    }

    public <T, H extends Holder<T>> SRegistry<H> create(Class<T> clazz) {
        return new SRegistry<>();
    }
}
