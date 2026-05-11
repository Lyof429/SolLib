package net.lcc.sollib.api.common.data.runtime;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class RuntimeData {
    protected Supplier<Boolean> activationRule;
    protected Function<String, String> function;
    protected boolean ephemeral;

    public RuntimeData(Supplier<Boolean> activationRule, UnaryOperator<String> function) {
        this.activationRule = activationRule;
        this.function = function;
        this.ephemeral = false;
    }

    public RuntimeData withEphemeral(boolean persistent) {
        this.ephemeral = persistent;
        return this;
    }

    public boolean isEphemeral() {
        return this.ephemeral;
    }

    public @Nullable String apply(@Nullable String original) {
        if (this.activationRule.get()) return this.function.apply(original);
        return original;
    }
}
