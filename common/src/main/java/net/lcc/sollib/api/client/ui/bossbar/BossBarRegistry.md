# SolClientRegistries.BOSS_BAR
### SBossBarRegistry

----

Main class to register custom boss bar rendering.

---

### `void register(Predicate<BossEvent> condition, IBossBarRenderer renderer)`

Registers a custom renderer for boss bars.

Any BossEvent which satisfies `@param condition` will be affected.

```java
SolClientRegistries.BOSS_BAR.register(bossEvent -> bossEvent.getName().contains(Component.empty()), (guiGraphics, x, y, bossEvent) -> {
    guiGraphics.blit(texture, x, y, 0, bossEvent.getColor().ordinal() * 5 * 2 + 5, (int)(bossEvent.getProgress() * 183.0F), 5);
    guiGraphics.blit(texture, x, y - 2, 0, 0, 183, 9, 183, 9);

    int progressWidth = (int) (bossEvent.getProgress() * 183.0F);
    if (progressWidth > 0) {
                guiGraphics.blit(texture, x, y - 2, 0, 9, progressWidth, 9, 183, 9);
    }
});
```

---

### `void register(Predicate<BossEvent> condition, ResourceLocation texture)`

Registers a custom renderer for boss bars.

Unlike `void register(Predicate<BossEvent> condition, IBossBarRenderer renderer)`, method automatically registers boss bar frame with default vanilla elements size.

---

### IBossBarRenderer

```java
void render(GuiGraphics guiGraphics, int x, int y, BossEvent bossEvent);
```

A functional interface for boss bar rendering.

- `guiGraphics` Renders the given BossBar at the given x and y.
- `x` The position of rendered element on abscissa axis.
- `y` The position of rendered element on ordinate axis.
- `bossEvent` Currently used BossEvent