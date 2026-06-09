# SolClientRegistries.Render.ITEM
### SItemRendererRegistry

----

Main class to register custom rendering for items.

---

### `void register(Predicate<ItemStack> condition, IItemRenderer renderer)`

Registers a custom dynamic renderer for items.

Any ItemStack which satisfies `@param condition` will be affected.

This method is automatically called for any Item implementing IAddedRenderItem.

PoseStack will automatically be pushed and then popped.

```java
// Define the texture somewhere else to avoid rendering lag
ResourceLocation TEXTURE = SolLib.MOD.makeID("textures/models/staff/glint.png");

// Renders an extra white dot on enchanted diamond swords
SolClientRegistries.Render.ITEM.register(stack -> stack.is(Items.DIAMOND_SWORD) && stack.isEnchanted(),
        (stack, context, matrices, bufferSource, light, overlay) -> {
    matrices.scale(1.005f, 1.005f, 1.005f);
    matrices.translate(0, 0.995, 0.4975);
    matrices.mulPose(Axis.XP.rotationDegrees(180));

    MockItemRenderer.renderItem(matrices, bufferSource, light, TEXTURE);
});
```

---

### IItemRenderer

```java
void render(ItemStack stack, ItemDisplayContext displayContext, PoseStack matrices, MultiBufferSource bufferSource, int packedLight, int packedOverlay);
```

A functional interface for item rendering.
- `stack` The currently rendered ItemStack, for eventual extra checks or processing.
- `displayContext` The currently used ItemDisplayContext.
- `poseStack` Currently used PoseStack.
- `bufferSource` Currently used MultiBufferSource.
- `packedLight` The light currently shed on the item.
- `packedOverlay` The overlay currently shed on the item.

### IAddedRenderItem

```java
void render(ItemStack stack, ItemDisplayContext displayContext, PoseStack matrices, MultiBufferSource bufferSource, int packedLight, int packedOverlay);
boolean shouldAddRender(ItemStack stack);
```

An extension of IItemRenderer with a built in rendering predicate.

Any Item implementing this will have their renderer automatically registered.

---

### IAddedBarItem

```java
boolean shouldAddBarRender(ItemStack stack);
float getAddedBarFullness(ItemStack stack);
int getAddedBarColor(ItemStack stack);
```

Any item implementing this will render a second durability bar.
- Will only be visible when `shouldAddBarRender` returns true.
- The bar's fullness is controlled by `getBarFullness`, which should range from 0 (empty) to 1 (full).
- The bar's color is controlled by `getAddedBarColor`, which should return a packed int.