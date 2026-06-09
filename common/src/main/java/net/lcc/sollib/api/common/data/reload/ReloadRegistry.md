# SolRegistries.Data.RELOAD
### SReloadRegistry

----

Main class to register reload listeners.

---

### `void register(IReloadListener listener)`

Registers a new reload listener.

`listener.reload(ResourceManager manager)` will be called **after** vanilla reloading.

`listener.preload(ResourceManager manager)` will be called **before** vanilla reloading.
Only really useful if you plan to setup RuntimeData or need things to be loaded before vanilla can read data.

These methods will be called both on the client (assets) and server (data), so make sure you're filtering what you read!

```java
// Can actually define a new class
public static class TestReloader implements IReloadListener {
    @Override
    public void preload(ResourceManager manager) {
        SolLib.MOD.getLogger().info("preload");
    }

    @Override
    public void reload(ResourceManager manager) {
        SolLib.MOD.getLogger().info("reload");
    }
}

SolRegistries.Data.RELOAD.register(new TestReloader());

// Or make full use of the fact IReloadListener is a functional interface
SolRegistries.Data.RELOAD.register(manager -> {
        SolLib.MOD.getLogger().info("reload");
});
```

### IReloadListener

```java
default void preload(ResourceManager manager) {}
void reload(ResourceManager manager);
```

`preload` is {} by default to make the class a functional interface, allowing syntax sugar in case it is not needed.

This interface also has utility methods to quickly read a Resource as a JsonObject (which will return null if it fails).

```java
static JsonObject open(Map.Entry<ResourceLocation, Resource> entry);
static JsonObject open(ResourceLocation id, Resource resource);
```

---

## SimpleDataRegistry\<T>

A helper class and simple use of IReloadListener.

Make a new instance using `public SimpleDataRegistry(String path, Reader<T> reader)`.
It will automatically fire on every resource reload and grab all resources in `path`, read them using `reader`, and store the resulting values.

Values can then be accessed using `get(ResourceLocation id)` or `getAll()`.

Can be useful to quickly declare new data registries while avoiding boilerplate.

### Reader\<T>

Very basically an alias for BiFunction<ResourceLocation, JsonObject, T>.