package net.lcc.sollib.api.common.data.runtime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.lcc.sollib.SolLib;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.apache.commons.io.input.CharSequenceInputStream;
import org.jetbrains.annotations.ApiStatus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SRuntimeRegistry {
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected Map<ResourceLocation, List<RuntimeData>> INSTANCES = new HashMap<>();

    public void addRemoval(ResourceLocation target, Supplier<Boolean> activationRule) {
        addText(target, original -> null, activationRule);
    }

    public void addJson(ResourceLocation target, Function<JsonObject, JsonObject> function) {
        addJson(target, function, () -> true);
    }

    public void addJson(ResourceLocation target, Function<JsonObject, JsonObject> function, Supplier<Boolean> activationRule) {
        addText(target, original -> {
            try {
                return GSON.toJson(function.apply(GSON.fromJson(original, JsonObject.class)));
            } catch (Exception ignored) {
                return original;
            }
        }, activationRule);
    }

    public void addText(ResourceLocation target, Function<String, String> function) {
        addText(target, function, () -> true);
    }

    public void addText(ResourceLocation target, Function<String, String> function, Supplier<Boolean> activationRule) {
        if (!INSTANCES.containsKey(target)) INSTANCES.put(target, new ArrayList<>());
        INSTANCES.get(target).add(new RuntimeData(activationRule, function));
    }


    @ApiStatus.Internal
    public Resource apply(ResourceLocation target, Resource original) {
        if (!INSTANCES.containsKey(target)) return original;
        if (original != null && original.source() instanceof RuntimeResourcePack) return original;

        SolLib.LOG.info("Applying configured data:", target);

        String result;
        try {
            result = original == null ? null : new String(original.open().readAllBytes());
        } catch (IOException ignored) {
            result = null;
        }

        for (RuntimeData data : INSTANCES.get(target)) {
            try {
                result = data.apply(result);
            } catch (Exception e) {
                SolLib.LOG.error(e);
            }
        }

        final String finalResult = result;
        return finalResult == null ? null : new Resource(RuntimeResourcePack.INSTANCE,
                () -> new ByteArrayInputStream(finalResult.getBytes()));
    }

    @ApiStatus.Internal
    public List<ResourceLocation> findMatching(String startingPath, Predicate<ResourceLocation> allowedPathPredicate) {
        List<ResourceLocation> matching = new ArrayList<>();
        for (ResourceLocation id : INSTANCES.keySet()) {
            if (id.getPath().startsWith(startingPath + "/") && allowedPathPredicate.test(id))
                matching.add(SolLib.LOG.info(id));
        }
        return matching;
    }
}
