package net.lcc.sollib.api.common.weather;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TimeArgument;
import org.jetbrains.annotations.ApiStatus;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class SWeatherCommandRegistry {
    public static final SWeatherCommandRegistry INSTANCE = new SWeatherCommandRegistry();
    public static final int DEFAULT_DURATION = -1;

    private final Map<String, Entry> INSTANCES = new LinkedHashMap<>();

    /**
     * Registers a new weather type to the /weather command
     * @param name The subcommand name
     * @param action A consumer taking the source and the duration (in ticks)
     * @since 1.0
     */
    public void register(String name, BiConsumer<CommandSourceStack, Integer> action) {
        this.register(name, action, (source, duration) -> action.accept(source, 0));
    }

    public void register(String name, BiConsumer<CommandSourceStack, Integer> action, BiConsumer<CommandSourceStack, Integer> clearAction) {
        INSTANCES.put(name, new Entry(action, clearAction));
    }

    /**
     * Internal method to inject all registered weather types into the builder
     * @since 1.0
     */
    @ApiStatus.Internal
    public void apply(LiteralArgumentBuilder<CommandSourceStack> builder) {
        for (Map.Entry<String, Entry> entry : INSTANCES.entrySet()) {
            BiConsumer<CommandSourceStack, Integer> action = entry.getValue().action();

            builder.then(
                    Commands.literal(entry.getKey())
                            .executes(ctx -> {
                                action.accept(ctx.getSource(), DEFAULT_DURATION);
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
    public void clear(CommandSourceStack source, int duration) {
        INSTANCES.values().forEach(entry -> entry.clearAction().accept(source, duration));
    }

    private record Entry(BiConsumer<CommandSourceStack, Integer> action, BiConsumer<CommandSourceStack, Integer> clearAction) {
    }
}