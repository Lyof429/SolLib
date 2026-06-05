package net.lcc.sollib.api.common.data.reload;

import com.google.gson.JsonObject;
import net.lcc.sollib.api.common.SolRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SimpleDataRegistry<T> {
    @FunctionalInterface
    public interface Reader<T> {
        T read(ResourceLocation id, JsonObject json);
    }

    protected final String path;
    protected final Reader<T> reader;

    protected final Map<ResourceLocation, T> values;

    public SimpleDataRegistry(String path, Reader<T> reader) {
        this.path = path;
        this.reader = reader;

        this.values = new HashMap<>();
        SolRegistries.Data.RELOAD.register(manager -> {
            this.values.clear();
            FileToIdConverter finder = FileToIdConverter.json(path);

            for (Map.Entry<ResourceLocation, Resource> entry : finder.listMatchingResources(manager).entrySet()) {
                JsonObject json = IReloadListener.open(entry);
                if (json == null) continue;

                this.values.put(finder.fileToId(entry.getKey()), this.reader.read(entry.getKey(), json));
            }
        });
    }

    public T get(ResourceLocation id) {
        return this.values.get(id);
    }

    public Set<Map.Entry<ResourceLocation, T>> getAll() {
        return this.values.entrySet();
    }
}
