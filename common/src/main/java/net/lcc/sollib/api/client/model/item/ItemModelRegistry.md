# SolClientRegistries.ITEM_MODEL
### SItemModelRegistry

----

Main class to register custom model types for items.

---

### `registerHeld(ItemLike item)`

Registers the item to have a different model when held in hand (akin to tridents and spyglasses).

The held model for `namespace:id` must be located at `assets/<namespace>/models/item/<id>_in_hand.json`.

The base missing model will be used in case it can't be found, and a warning will be logged.

```java
// Sets minecraft:diamond to have a custom held model
SolClientRegistries.ITEM_MODEL.registerHeld(Items.DIAMOND);
```

---