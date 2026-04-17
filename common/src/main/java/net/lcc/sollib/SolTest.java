package net.lcc.sollib;

import com.google.gson.JsonPrimitive;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.config.IConfigurable;
import net.lcc.sollib.api.common.config.JsonBuilder;
import net.lcc.sollib.api.common.config.SolConfig;
import net.lcc.sollib.api.common.data.reload.IReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class SolTest {
    public record Thing(double x, String name) implements IConfigurable {
        @Override
        public void toJson(JsonBuilder builder) {
            builder.add("name", name).add("x", x);
        }

        @Override
        public String toString() {
            return "Thing{" +
                    "x=" + x +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static class TestReloader implements IReloadListener {
        @Override
        public void preload(ResourceManager manager) {
            SolLib.LOG.info("preload");
        }

        @Override
        public void reload(ResourceManager manager) {
            SolLib.LOG.info("reload");
        }
    }

    public static SolConfig CONFIG;

    public static void lyof() {
        ConfigEntry<String> hello = new ConfigEntry<>("world");
        ConfigEntry<Boolean> exists = new ConfigEntry<>(true);
        ConfigEntry<Thing> another = new ConfigEntry<>(new Thing(-4, "error")).withProcessor(elm ->
                new Thing(elm.getAsJsonObject().get("x").getAsDouble(), elm.getAsJsonObject().get("name").getAsString()));

        IConfigurable builder = it -> it
                .addCategory("test_category", a -> a
                        .comment("This is a comment")
                        .add("hello", "world")
                        .bind(hello)
                        .addCategory("nested", b -> b
                                .comment("Supports string, number and boolean values by default")
                                .add("exists", true)
                                .bind(exists))
                        .addCategory("another", b -> b
                                .comment("Ah and lists of them too I forgot about that")
                                .comment("  (Lists don't have to hold a single type btw)")
                                .addList("michel", c -> c
                                        .add(2)
                                        .add("this is a list, in case you didn't notice")
                                        .addCategory(new Thing(7, "luigi"))
                                        .add(12))))
                .addCategory("thing", new Thing(3, "mario"))
                .bind(another);
        CONFIG = new SolConfig("sollib/test", 1.0, builder);
        CONFIG.init();

        SolRegistries.RELOADER.register(new TestReloader());

        ResourceLocation id = ResourceLocation.tryBuild("minecraft", "recipes/bucket.json");
        SolRegistries.RUNTIME.addText(id, SolLib.LOG::warn);
        SolRegistries.RUNTIME.addJson(id, original -> {
            if (original != null)
                original.getAsJsonObject("key")
                        .getAsJsonObject("#")
                        .add("item", new JsonPrimitive("minecraft:gold_ingot"));
            return original;
        });
        SolRegistries.RUNTIME.addText(id, SolLib.LOG::warn);

        SolRegistries.RUNTIME.addText(ResourceLocation.tryBuild("minecraft", "recipes/patrick.json"), original -> "{\"type\":\"minecraft:crafting_shaped\",\"category\":\"misc\",\"key\":{\"#\":{\"item\":\"minecraft:diamond\"}},\"pattern\":[\"# #\",\" # \"],\"result\":{\"item\":\"minecraft:bucket\"},\"show_notification\":true}");
        SolRegistries.RUNTIME.addRemoval(ResourceLocation.tryBuild("minecraft", "recipes/diamond_pickaxe.json"), () -> true);
    }
}
