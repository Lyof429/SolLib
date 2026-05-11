package net.lcc.sollib.api.common.data.runtime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.lcc.sollib.api.common.logger.SolLogger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.jetbrains.annotations.ApiStatus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class SRuntimeRegistry {
    public static final SRuntimeRegistry INSTANCE = new SRuntimeRegistry();
    private SRuntimeRegistry() {}

    protected static final SolLogger LOG = new SolLogger("SolLib/Data/Runtime");
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Map<ResourceLocation, List<RuntimeData>> instances = new HashMap<>();

    /**
     * Dynamically removes the specified data if activationRule is met (on each reload)
     */
    public RuntimeData addRemoval(ResourceLocation target, Supplier<Boolean> activationRule) {
        return addText(target, original -> null, activationRule);
    }

    /**
     * Dynamically changes the specified data if activationRule is met (on each reload) <br/>
     * In case the targeted data doesn't exist, a null value will be passed to function
     */
    public RuntimeData addJson(ResourceLocation target, UnaryOperator<JsonObject> function) {
        return addJson(target, function, () -> true);
    }

    /**
     * Dynamically changes the specified data if activationRule is met (on each reload) <br/>
     * Only applied if activationRule is met for that specific reload <br/>
     * In case the targeted data doesn't exist, a null value will be passed to function
     */
    public RuntimeData addJson(ResourceLocation target, UnaryOperator<JsonObject> function, Supplier<Boolean> activationRule) {
        return addText(target, original -> {
            try {
                return GSON.toJson(function.apply(GSON.fromJson(original, JsonObject.class)));
            } catch (Exception ignored) {
                return original;
            }
        }, activationRule);
    }

    /**
     * Dynamically changes the specified data if activationRule is met (on each reload) <br/>
     * In case the targeted data doesn't exist, a null value will be passed to function
     */
    public RuntimeData addText(ResourceLocation target, UnaryOperator<String> function) {
        return addText(target, function, () -> true);
    }

    /**
     * Dynamically changes the specified data if activationRule is met (on each reload) <br/>
     * Only applied if activationRule is met for that specific reload <br/>
     * In case the targeted data doesn't exist, a null value will be passed to function
     */
    public RuntimeData addText(ResourceLocation target, UnaryOperator<String> function, Supplier<Boolean> activationRule) {
        if (!instances.containsKey(target)) instances.put(target, new ArrayList<>());
        RuntimeData data = new RuntimeData(activationRule, function);
        instances.get(target).add(data);
        return data;
    }


    @ApiStatus.Internal
    public Resource apply(ResourceLocation target, Resource original) {
        if (!instances.containsKey(target)) return original;
        if (original != null && original.source() instanceof RuntimeResourcePack) return original;

        LOG.info("Applying configured data:", target);

        String result;
        try {
            result = original == null ? null : new String(original.open().readAllBytes());
        } catch (IOException ignored) {
            result = null;
        }

        for (RuntimeData data : instances.get(target)) {
            try {
                result = data.apply(result);
            } catch (Exception e) {
                LOG.error(e);
            }
        }

        final String finalResult = result;
        return finalResult == null ? null : new Resource(RuntimeResourcePack.INSTANCE,
                () -> new ByteArrayInputStream(finalResult.getBytes()));
    }

    @ApiStatus.Internal
    public List<ResourceLocation> findMatching(String startingPath, Predicate<ResourceLocation> allowedPathPredicate) {
        List<ResourceLocation> matching = new ArrayList<>();
        for (ResourceLocation id : instances.keySet()) {
            if (id.getPath().startsWith(startingPath + "/") && allowedPathPredicate.test(id))
                matching.add(id);
        }
        return matching;
    }

    @ApiStatus.Internal
    public void clean() {
        instances.forEach((id, list) -> list.removeIf(RuntimeData::isEphemeral));
    }
}
