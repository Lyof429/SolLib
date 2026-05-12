package net.lcc.sollib.api.common.worldgen.biome;

import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.data.reload.IReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

public class DimensionReloader implements IReloadListener {
    @Override
    public void preload(ResourceManager manager) {
        SolRegistries.BIOME.apply(manager, this::open);
    }

    @Override
    public void reload(ResourceManager manager) {}
}
