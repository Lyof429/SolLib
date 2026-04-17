package net.lcc.sollib.api.common.weather;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TimeArgument;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class SWeatherCommandRegistry {
    private final Map<String, BiConsumer<CommandSourceStack, Integer>> INSTANCES = new HashMap<>();

    /**
     * Registers a new weather type to the /weather command
     * @param name The subcommand name
     * @param action A consumer taking the source and the duration (in ticks)
     * @since 1.0.0
     */
    public void register(String name, BiConsumer<CommandSourceStack, Integer> action) {
        INSTANCES.put(name, action);
    }

    /**
     * Internal method to inject all registered weather types into the builder
     * @since 1.0.0
     */
    @ApiStatus.Internal
    public void apply(LiteralArgumentBuilder<CommandSourceStack> builder) {
        for (Map.Entry<String, BiConsumer<CommandSourceStack, Integer>> entry : INSTANCES.entrySet()) {
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

    @ApiStatus.Internal
    public void clear(CommandSourceStack source) {
        INSTANCES.values().forEach(action -> action.accept(source, 0));
    }
}