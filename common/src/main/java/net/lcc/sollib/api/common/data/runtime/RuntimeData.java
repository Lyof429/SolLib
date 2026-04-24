package net.lcc.sollib.api.common.data.runtime;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public class RuntimeData {
    protected Supplier<Boolean> activationRule;
    protected Function<String, String> function;

    public RuntimeData(Supplier<Boolean> activationRule, Function<String, String> function) {
        this.activationRule = activationRule;
        this.function = function;
    }

    public @Nullable String apply(@Nullable String original) {
        if (this.activationRule.get()) return this.function.apply(original);
        return original;
    }
}
