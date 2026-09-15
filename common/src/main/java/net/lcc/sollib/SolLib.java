package net.lcc.sollib;

import com.google.common.collect.Iterables;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.config.SolConfig;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.api.common.registry.holder.DensityFunctionHolder;
import net.lcc.sollib.api.common.registry.holder.EffectHolder;
import net.lcc.sollib.api.common.worldgen.density.ProgressionDensityFunction;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class SolLib {
    public static final String MOD_ID = "sollib";
    public static final SolModContainer MOD = new SolModContainer("SolLib", MOD_ID) {
        @Override
        public Iterable<SolConfig> getConfigs() {
            return Iterables.concat(super.getConfigs(), SolRegistries.CONFIG.getAll());
        }
    };

    public static class Effect extends MobEffect {
        protected Effect(MobEffectCategory category, int color) {
            super(category, color);
        }
    }

    public static void init() {
        MOD.register(DensityFunctionHolder.class, "progression", () -> ProgressionDensityFunction.CODEC);

        EffectHolder ROLLING = MOD.register(EffectHolder.class, "rolling",
                () -> new Effect(MobEffectCategory.NEUTRAL, 0xf0faf0));
    }
}