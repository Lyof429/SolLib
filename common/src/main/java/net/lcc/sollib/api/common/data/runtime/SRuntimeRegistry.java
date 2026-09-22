package net.lcc.sollib.api.common.data.runtime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.config.SolConfig;
import net.lcc.sollib.api.common.logger.SolLogger;
import net.lcc.sollib.api.event.SEventListener;
import net.lcc.sollib.core.Identifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import org.jetbrains.annotations.ApiStatus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class SRuntimeRegistry implements SEventListener {
    public static final SRuntimeRegistry INSTANCE = new SRuntimeRegistry();

    public static final SolLogger LOG = new SolLogger("Sol/Data/Runtime");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Map<ResourceLocation, List<RuntimeData>> instances = new HashMap<>();

    private static final ConfigEntry<Set<ResourceLocation>> blacklist = new ConfigEntry<Set<ResourceLocation>>(Set.of()).withProcessor(json -> {
        Set<ResourceLocation> result = new HashSet<>();
        for (JsonElement elm : json.getAsJsonArray())
            result.add(Identifier.of(elm.getAsString()));
        return result;
    });
    private static final ConfigEntry<Boolean> enableLog = new ConfigEntry<>(true);

    /**
     * Dynamically removes the specified data if activationRule is met (on each reload)
     */
    public RuntimeData addRemoval(ResourceLocation target, Supplier<Boolean> activationRule) {
        return addText(target, original -> null, activationRule);
    }

    /**
     * Dynamically adds the specified data if it doesn't exist already (on each reload)
     */
    public RuntimeData addDefault(ResourceLocation target, String content) {
        return addDefault(target, content, () -> true);
    }

    /**
     * Dynamically adds the specified data if activationRule is met and it doesn't exist already (on each reload)
     */
    public RuntimeData addDefault(ResourceLocation target, String content, Supplier<Boolean> activationRule) {
        return addText(target, original -> original == null ? content : original, activationRule);
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
        if (original != null && original.source() instanceof RuntimeResourcePack) return original;
        if (!instances.containsKey(target)) return original;
        if (blacklist.get().contains(target)) {
            if (enableLog.get())
                LOG.info("Skipped runtime data \"" + target + "\" because it was blacklisted in configs");
            return original;
        }

        if (enableLog.get())
            LOG.info("Applying runtime data:", target);

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

    private static final BiConsumer<ResourceLocation, List<RuntimeData>> sol_removeEphemeral
            = (id, list) -> list.removeIf(RuntimeData::isEphemeral);
    private static final Predicate<Map.Entry<ResourceLocation, List<RuntimeData>>> sol_isEmpty
            = entry -> entry.getValue().isEmpty();

    @ApiStatus.Internal
    public void clean() {
        instances.forEach(sol_removeEphemeral);
        instances.entrySet().removeIf(sol_isEmpty);
    }

    @Override
    public void onConfigBuild(SolConfig.BuildEvent event) {
        if (!event.configName().equals(SolLib.MOD_ID)) return;

        event.builder().addObject("runtime_data", config -> config
                .comment("If false, disables \"Applying runtime data\" messages in logs")
                .add("log", true)
                .comment("Any file id in this list will be ignored when applying runtime data")
                .comment("  It should never be the case, but if a file fails to load properly, try adding it here and see if it fixes the issue")
                .bind(enableLog)
                .addArray("blacklist", List.of())
                .bind(blacklist)
        );
    }
}
