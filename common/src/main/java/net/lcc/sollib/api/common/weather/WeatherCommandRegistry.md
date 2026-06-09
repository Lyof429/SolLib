# SolRegistries.WEATHER
### SWeatherCommandRegistry

----

Main class to register custom weather command.

---

### `void register(String name, BiConsumer<CommandSourceStack, Integer> action)`

Registers a custom weather type into /weather command.

```java
SolRegistries.WEATHER.register("inferno", (source, duration) -> {
    if (duration <= 0) 
        return;

    source.sendSuccess(() -> Component.literal("Set weather to Inferno for " + duration + " ticks"), true);
});
```

- `name` The name of parameter for /weather command (e.g. /weather rain).
- `source` The result action of command execution.
- `duraction` An amount of time in ticks.