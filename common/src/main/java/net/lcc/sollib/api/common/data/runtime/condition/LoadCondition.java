package net.lcc.sollib.api.common.data.runtime.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.data.runtime.SRuntimeRegistry;
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
 * - "not": Takes another condition in a <i>value</i> field and inverts it. <br/>
 * - "and": Takes a list of conditions in a <i>values</i> field. Yields true only if every sub condition is true. <br/>
 * - "or": Takes a list of conditions in a <i>values</i> field. Yields true if at least one sub condition is true.
 * <br/> <br/>
 *
 * For example:
 * <pre>
 * {
 *   "message": "Hello World!",
 *   "sollib:load_condition": {
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
public class LoadCondition {
    public static final ResourceLocation CONFIG = SolLib.MOD.makeID("config");

    public static boolean configMatches(JsonObject json) {
        try {
            return SolRegistries.CONFIG.get(GsonHelper.getAsString(json, "entry"), true);
        } catch (Exception e) {
            return true;
        }
    }


    public static Resource apply(ResourceLocation id, Resource resource) {
        if (id.getPath().endsWith(".json") && resource != null) {
            try {
                JsonElement elm = SRuntimeRegistry.GSON.fromJson(new String(resource.open().readAllBytes()), JsonElement.class);
                if (!elm.isJsonObject()) return resource;
                JsonObject json = elm.getAsJsonObject();
                if (!json.has("sollib:load_condition")) return resource;

                JsonObject condition = GsonHelper.getAsJsonObject(json, "sollib:load_condition");
                return shouldLoad(condition) ? resource : null;
            } catch (Exception e) {
                SRuntimeRegistry.LOG.error("Error while reading load condition for data " + id);
                e.printStackTrace();
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
                configMatches(condition);
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
            default -> throw new IllegalArgumentException("Unknown condition type: " + type);
        };
    }
}
