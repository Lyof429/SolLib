package net.lcc.sollib.api.common.weather;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TimeArgument;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class SolWeatherCommandRegistry {
    private static final Map<String, BiConsumer<CommandSourceStack, Integer>> WEATHER_TYPES = new HashMap<>();

    /**
     * Registers a new weather type to the /weather command.
     * @param name The subcommand name (e.g., "grippfall")
     * @param action A consumer taking the source and the duration (in ticks)
     */
    public static void register(String name, BiConsumer<CommandSourceStack, Integer> action) {
        WEATHER_TYPES.put(name, action);
    }
    /**
     * Internal method to inject all registered weather types into the builder.
     */
    public static void injectCustomWeather(LiteralArgumentBuilder<CommandSourceStack> builder) {
        for (Map.Entry<String, BiConsumer<CommandSourceStack, Integer>> entry : WEATHER_TYPES.entrySet()) {
            String name = entry.getKey();
            BiConsumer<CommandSourceStack, Integer> action = entry.getValue();

            builder.then(
                    Commands.literal(name)
                            .executes(ctx -> {
                                action.accept(ctx.getSource(), -1);
                                return 1;
                            })
                            .then(
                                    Commands.argument("duration", TimeArgument.time(1))
                                            .executes(ctx -> {
                                                int duration = IntegerArgumentType.getInteger(ctx, "duration");
                                                action.accept(ctx.getSource(), duration);
                                                return duration;
                                            })
                            )
            );
        }
    }

    public static Iterable<BiConsumer<CommandSourceStack, Integer>> getAllActions() {
        return WEATHER_TYPES.values();
    }
}