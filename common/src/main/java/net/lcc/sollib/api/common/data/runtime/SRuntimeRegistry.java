package net.lcc.sollib.api.common.data.runtime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.common.data.reload.SReloadRegistry;
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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class SRuntimeRegistry {
    public static final SRuntimeRegistry INSTANCE = new SRuntimeRegistry();
    private SRuntimeRegistry() {}

    protected static final SolLogger LOG = new SolLogger("SolLib/Data/Runtime");
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Map<ResourceLocation, List<RuntimeData>> INSTANCES = new HashMap<>();

    /**
     * Dynamically removes the specified data if activationRule is met (on each reload)
     */
    public void addRemoval(ResourceLocation target, Supplier<Boolean> activationRule) {
        addText(target, original -> null, activationRule);
    }

    /**
     * Dynamically changes the specified data if activationRule is met (on each reload) <br/>
     * In case the targeted data doesn't exist, a null value will be passed to function
     */
    public void addJson(ResourceLocation target, UnaryOperator<JsonObject> function) {
        addJson(target, function, () -> true);
    }

    /**
     * Dynamically changes the specified data if activationRule is met (on each reload) <br/>
     * Only applied if activationRule is met for that specific reload <br/>
     * In case the targeted data doesn't exist, a null value will be passed to function
     */
    public void addJson(ResourceLocation target, UnaryOperator<JsonObject> function, Supplier<Boolean> activationRule) {
        addText(target, original -> {
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
    public void addText(ResourceLocation target, UnaryOperator<String> function) {
        addText(target, function, () -> true);
    }

    /**
     * Dynamically changes the specified data if activationRule is met (on each reload) <br/>
     * Only applied if activationRule is met for that specific reload <br/>
     * In case the targeted data doesn't exist, a null value will be passed to function
     */
    public void addText(ResourceLocation target, UnaryOperator<String> function, Supplier<Boolean> activationRule) {
        if (!INSTANCES.containsKey(target)) INSTANCES.put(target, new ArrayList<>());
        INSTANCES.get(target).add(new RuntimeData(activationRule, function));
    }


    @ApiStatus.Internal
    public Resource apply(ResourceLocation target, Resource original) {
        if (!INSTANCES.containsKey(target)) return original;
        if (original != null && original.source() instanceof RuntimeResourcePack) return original;

        LOG.info("Applying configured data:", target);

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
        for (ResourceLocation id : INSTANCES.keySet()) {
            if (id.getPath().startsWith(startingPath + "/") && allowedPathPredicate.test(id))
                matching.add(id);
        }
        return matching;
    }
}
