# SolClientRegistries.Render.BLOCK
### SBlockRendererRegistry

----

Main class to register custom rendering for blocks.

---

### `void register(Predicate<BlockState> condition, IBlockRenderer renderer)`

Registers a custom dynamic renderer for blocks.

Any BlockState which satisfies `@param condition` will be affected.

This method is automatically called for any Block implementing IAddedRenderBlock.

```java
// Renders an extra azalea bush on fully grown bamboos
SolClientRegistries.Render.BLOCK.register(state -> state.is(Blocks.BAMBOO)
            && state.getValue(BlockStateProperties.BAMBOO_LEAVES).equals(BambooLeaves.LARGE),
        (renderer, state, pos, getter, poseStack, vertexConsumer, random) -> {
    BlockState azaleaState = Blocks.AZALEA.defaultBlockState();
    renderer.renderBlock(pos, azaleaState);
});
```

---

### IBlockRenderer

`void render(BiConsumer<BlockPos, BlockState> renderer, BlockState state, BlockPos pos, BlockAndTintGetter getter,
                RandomSource random, @Nullable PoseStack poseStack, @Nullable VertexConsumer vertexConsumer);`

A functional interface for block rendering.
- `renderer` Renders the given BlockState at the given BlockPos. Implementation varies depending on Sodium presence.
- `state` The currently rendered BlockState, for eventual extra checks or processing.
- `pos` The BlockPos of the currently rendered BlockState.
- `getter` A world access valid in the chunk around `pos`.
- `random` A random access.
- `poseStack` Currently used PoseStack. Will be null if Sodium rendering is used.
- `vertexConsumer` Currently used VertexConsumer. Will be null if Sodium rendering is used.

### IAddedRenderBlock

An extension of IBlockRenderer with a built in rendering predicate.

Any Block implementing this will have their renderer automatically registered.