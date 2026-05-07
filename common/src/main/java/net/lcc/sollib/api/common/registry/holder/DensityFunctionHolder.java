package net.lcc.sollib.api.common.registry.holder;

import com.mojang.serialization.Codec;
import net.lcc.sollib.api.common.registry.Holder;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.DensityFunction;

import java.util.function.Supplier;

public class DensityFunctionHolder extends Holder<Codec<? extends DensityFunction>> {
    public DensityFunctionHolder(SolModContainer mod, String name, Supplier<Codec<? extends DensityFunction>> entrySupplier) {
        super(mod, name, entrySupplier);
    }

    @Override
    protected Registry<Codec<? extends DensityFunction>> getRegistry() {
        return BuiltInRegistries.DENSITY_FUNCTION_TYPE;
    }
}
