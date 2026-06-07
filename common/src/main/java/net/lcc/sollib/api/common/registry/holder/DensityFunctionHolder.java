package net.lcc.sollib.api.common.registry.holder;

import com.mojang.serialization.MapCodec;
import net.lcc.sollib.api.common.registry.SHolder;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.function.Supplier;

public class DensityFunctionHolder extends SHolder<MapCodec<? extends DensityFunction>> {
    public DensityFunctionHolder(SolModContainer mod, String name, Supplier<MapCodec<? extends DensityFunction>> entrySupplier) {
        super(mod, name, entrySupplier);
    }

    @Override
    public Registry<MapCodec<? extends DensityFunction>> getRegistry() {
        return BuiltInRegistries.DENSITY_FUNCTION_TYPE;
    }
}
