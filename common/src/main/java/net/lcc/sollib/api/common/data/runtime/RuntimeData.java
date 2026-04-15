package net.lcc.sollib.api.common.data.runtime;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public class RuntimeData {
    protected ResourceLocation target;
    protected Supplier<Boolean> activationRule;
    protected Function<String, String> function;

    public RuntimeData(ResourceLocation target, Supplier<Boolean> activationRule, Function<String, String> function) {
        this.target = target;
        this.activationRule = activationRule;
        this.function = function;
    }

    public ResourceLocation getTarget() {
        return this.target;
    }

    public @Nullable String apply(@Nullable String original) {
        if (this.activationRule.get()) return this.function.apply(original);
        return original;
    }
}
