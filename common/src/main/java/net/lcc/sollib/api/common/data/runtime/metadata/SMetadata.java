package net.lcc.sollib.api.common.data.runtime.metadata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.data.runtime.SRuntimeRegistry;
import net.lcc.sollib.core.Identifier;
import net.lcc.sollib.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.util.GsonHelper;

import java.io.InputStream;

/**
 * Any data/resource pack file can be given additional metadata by putting a file with the same name and an added .sol next to it
 * <br/> For example, <i>yourmod:random/path/awesomefile.json</i> reads its metadata from <i>yourmod:random/path/awesomefile.json.sol</i> <b>in the same pack</b>
 * <br/> <br/>
 * This metadata file allows defining things such as load conditions or dynamic remaps
 */
public record SMetadata(JsonObject condition, JsonObject remaps) {
    public static final SMetadata EMPTY = new SMetadata(new JsonObject(), new JsonObject());

    public static SMetadata getFor(ResourceLocation id, PackResources pack, PackType type) {
        ResourceLocation meta = Identifier.of(id.getNamespace(), id.getPath() + ".sol");
        IoSupplier<InputStream> metaSupplier = pack.getResource(type, meta);
        if (metaSupplier == null) return EMPTY;

        try {
            JsonObject metadata = SRuntimeRegistry.GSON.fromJson(new String(metaSupplier.get().readAllBytes()), JsonObject.class);
            return new SMetadata(
                    GsonHelper.getAsJsonObject(metadata, "load_condition", new JsonObject()),
                    GsonHelper.getAsJsonObject(metadata, "remaps", new JsonObject())
            );
        } catch (Exception e) {
            SRuntimeRegistry.LOG.error("Error while reading metadata for " + id + " in pack " + pack.packId() + '\n' + e);
        }
        return EMPTY;
    }


    /**
     * To add load conditions to a data/resource pack file, add a "load_condition" object to its metadata
     * <br/> <br/>
     *
     * There currently are 5 types of conditions: <br/>
     * - "dependency": Takes an extra <i>mod</i> argument. File will only be loaded if said namespace is running. <br/>
     * - "config": Takes an extra <i>entry</i> argument. File will only be loaded if the corresponding {@link ConfigEntry} evaluates to true.
     * - "not": Takes another condition in a <i>value</i> field and inverts it. <br/>
     * - "and": Takes a list of conditions in a <i>values</i> field. Yields true only if every sub condition is true. <br/>
     * - "or": Takes a list of conditions in a <i>values</i> field. Yields true if at least one sub condition is true.
     * <br/> <br/>
     *
     * For example:
     * <pre>
     * {
     *   // rest of your metadata file
     *   "load_condition": {
     *     "type": "and",
     *     "values": [
     *       {
     *         "type": "config",
     *         "entry": "sollib/test:test_category.nested.exists"
     *       },
     *       {
     *         "type": "not",
     *         "value: {
     *           "type": "dependency",
     *           "mod": "fabric"
     *         }
     *       }
     *     ]
     *   }
     * }
     * </pre>
     */
    public static class Condition {
        public static boolean shouldLoad(ResourceLocation id, PackResources pack, PackType type) {
            return shouldLoad(SMetadata.getFor(id, pack, type).condition());
        }

        public static boolean shouldLoad(JsonObject condition) {
            if (!condition.has("type")) return true;

            String type = condition.get("type").getAsString();
            return switch (type) {
                case "dependency" ->
                        Services.PLATFORM.isModLoaded(GsonHelper.getAsString(condition, "mod"));
                case "config" -> {
                    try {
                        yield  SolRegistries.CONFIG.get(GsonHelper.getAsString(condition, "entry"), true);
                    } catch (Exception ignored) {
                        yield true;
                    }
                }
                case "not" ->
                        !shouldLoad(GsonHelper.getAsJsonObject(condition, "value"));
                case "and" -> {
                    for (JsonElement c : GsonHelper.getAsJsonArray(condition, "values")) {
                        if (!(c instanceof JsonObject o))
                            throw new IllegalArgumentException("Unknown condition object: " + c);
                        if (!shouldLoad(o))
                            yield false;
                    }
                    yield true;
                }
                case "or" -> {
                    for (JsonElement c : GsonHelper.getAsJsonArray(condition, "values")) {
                        if (!(c instanceof JsonObject o))
                            throw new IllegalArgumentException("Unknown condition object: " + c);
                        if (shouldLoad(o))
                            yield true;
                    }
                    yield false;
                }
                default -> {
                    SRuntimeRegistry.LOG.error("Unknown condition type: " + type);
                    yield true;
                }
            };
        }
    }


    public static class Remaps {

    }
}
