package net.lcc.sollib.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.lcc.sollib.api.common.weather.SolWeatherCommandRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.WeatherCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WeatherCommand.class)
public class WeatherCommandMixin {
    @WrapOperation(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/brigadier/CommandDispatcher;register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;"
            )
    )
    private static LiteralCommandNode<CommandSourceStack> injectAllCustomWeathers(
            CommandDispatcher<CommandSourceStack> dispatcher,
            LiteralArgumentBuilder<CommandSourceStack> builder,
            Operation<LiteralCommandNode<CommandSourceStack>> original
    ) {
        SolWeatherCommandRegistry.injectCustomWeather(builder);

        return original.call(dispatcher, builder);
    }

    @Inject(method = "setClear", at = @At("TAIL"))
    private static void onClearWeather(CommandSourceStack source, int duration, CallbackInfoReturnable<Integer> cir) {
        SolWeatherCommandRegistry.getAllActions().forEach(action -> action.accept(source, 0));
    }
}