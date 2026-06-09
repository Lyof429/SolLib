# ModelPartUtils

----

Adjuvant utility class for ModelPart operations.

---

### `ModelPart getPart(Triple<Float, Float, Float> partSize, List<ModelPart> parts)`

Captures the first model part which contains cuboid with exact same part size.

```java
if (this.getParentModel() instanceof IModelPartsAccessor accessor && !accessor.getAllModelParts().isEmpty()) {
    poseStack.pushPose();
    ModelPart modelPart = ModelPartUtils.getPart(getSizedPart(), accessor.getAllModelParts());
    if (modelPart != null && !modelPart.isEmpty())
        renderModelPartOverlay(modelPart, poseStack, vertexConsumerProvider, i, livingEntity);
    poseStack.popPose();
}
```

**List of model parts can be obtained by calling accessor.getAllModelParts() from IModelPartsAccessor instance.**

---

### `ModelPart getPart(String partName, List<ModelPart> parts)`

Captures the first model part which contains child with exact same name.

```java
if (this.getParentModel() instanceof IModelPartsAccessor accessor && !accessor.getAllModelParts().isEmpty()) {
    poseStack.pushPose();
    ModelPart modelPart = ModelPartUtils.getPart("head", accessor.getAllModelParts());
    if (modelPart != null && !modelPart.isEmpty())
        renderModelPartOverlay(modelPart, poseStack, vertexConsumerProvider, i, livingEntity);
    poseStack.popPose();
}
```

---

### `ModelPart getRandomPart(List<ModelPart> parts, RandomSource random)`

Captures a random model part from the model parts list.

Method is reimplementation from PlayerModel.

```java
if (this.getParentModel() instanceof IModelPartsAccessor accessor && !accessor.getAllModelParts().isEmpty()) {
    poseStack.pushPose();
    ModelPart modelPart = ModelPartUtils.getRandomPart(accessor.getAllModelParts(), random);
    if (modelPart != null && !modelPart.isEmpty())
        renderModelPartOverlay(modelPart, poseStack, vertexConsumerProvider, i, livingEntity);
    poseStack.popPose();
}
```

---

### IModelPartExtension

```java
Optional<ModelPart> findPartByName(String name);

Optional<ModelPart> findPartBySize(float x, float y, float z);

Map<String, ModelPart> getChildren();
```

An extension of ModelPart for gathering model parts by given properties.

---

### IModelPartsAccessor

```java
List<ModelPart> getAllModelParts();
```

An accessor for ModelPart to obtain all model parts contained in EntityModel.