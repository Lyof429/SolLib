package net.lcc.sollib.api.common.data.runtime;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lcc.sollib.SolTest;
import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.GsonHelper;

/**
 * <b>Any</b> datapack file can be given load conditions, and will only actually be read if they are met. <br>
 * To do so, simply add a <i>condition</i> field to the file, like so:
 * <pre>
 * {
 *   // Here be the rest of your json file
 *
 *   "condition": {
 *       "type": conditiontype
 *   }
 * }
 * </pre>
 *
 * There currently are 5 types of conditions: <br/>
 * - "dependency": Takes an extra <i>mod</i> argument. File will only be loaded if said namespace is running. <br/>
 * - "config": Takes an extra <i>entry</i> argument. File will only be loaded if the corresponding {@link ConfigEntry} evaluates to true.
 * - "not": Takes another condition in a <i>condition</i> field and inverts it. <br/>
 * - "and": Takes a list of conditions in a <i>conditions</i> field. Yields true only if every sub condition is true. <br/>
 * - "or": Takes a list of conditions in a <i>conditions</i> field. Yields true if at least one sub condition is true.
 * <br/> <br/>
 *
 * For example:
 * <pre>
 * {
 *   "message": "Hello World!",
 *   "condition": {
 *     "type": "and",
 *     "conditions": [
 *       {
 *         "type": "config",
 *         "entry": "sollib/test:test_category.nested.exists"
 *       },
 *       {
 *         "type": "not",
 *         "condition: {
 *           "type": "dependency",
 *           "mod": "fabric"
 *         }
 *       }
 *     ]
 *   }
 * }
 * </pre>
 */
public class LoadCondition {
    public static Resource apply(ResourceLocation id, Resource resource) {
        if (id.getPath().endsWith(".json")) {
            try {
                JsonObject json = SRuntimeRegistry.GSON.fromJson(new String(resource.open().readAllBytes()), JsonObject.class);

                JsonObject condition = GsonHelper.getAsJsonObject(json, "sollib:condition");
                return shouldLoad(condition) ? resource : null;
            } catch (Exception ignored) {
                SRuntimeRegistry.LOG.error("Error while reading condition for data " + id);
            }
        }
        return resource;
    }

    public static boolean shouldLoad(JsonObject condition) {
        String type = condition.get("type").getAsString();

        return switch (type) {
            case "dependency" ->
                Services.PLATFORM.isModLoaded(GsonHelper.getAsString(condition, "mod"));
            case "config" ->
                SolRegistries.CONFIG.get(GsonHelper.getAsString(condition, "entry"), true);
            case "not" ->
                    !shouldLoad(GsonHelper.getAsJsonObject(condition, "condition"));
            case "and" -> {
                for (JsonElement c : GsonHelper.getAsJsonArray(condition, "conditions")) {
                    if (!(c instanceof JsonObject o))
                        throw new IllegalArgumentException("Unknown condition object: " + c);
                    if (!shouldLoad(o))
                        yield false;
                }
                yield true;
            }
            case "or" -> {
                for (JsonElement c : GsonHelper.getAsJsonArray(condition, "conditions")) {
                    if (!(c instanceof JsonObject o))
                        throw new IllegalArgumentException("Unknown condition object: " + c);
                    if (shouldLoad(o))
                        yield true;
                }
                yield false;
            }
            default -> throw new IllegalArgumentException("Unknown condition type: " + type);
        };
    }
}
