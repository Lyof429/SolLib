package net.lcc.sollib;

import com.google.gson.JsonObject;
import net.lcc.sollib.api.SolRegistries;
import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.config.IConfigurable;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.EntityHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.List;

public class SolTest {
    public static final SolModContainer MOD = new SolModContainer("SolLib", "sollib");

    public static void lyof() {
        ConfigEntry<Integer> fuelValue = new ConfigEntry<>(5);

        ConfigEntry<String> hello = new ConfigEntry<>("world");
        ConfigEntry<JsonObject> exists = new ConfigEntry<>(new JsonObject());

        IConfigurable builder = it -> it
                .addObject("test_category", a -> a
                        .comment("This is a comment")
                        .add("hello", "minecraft:iron_pickaxe")
                        .bind(hello)
                        .addObject("nested", b -> b
                                .comment("Supports string, number and boolean values by default")
                                .add("exists", true)
                                .bind(exists))
                        .addObject("another", b -> b
                                .bind(exists)
                                .comment("Ah and lists of them too I forgot about that")
                                .comment("  (Lists don't have to hold a single type btw)")
                                .addArray("michel", c -> c
                                        .add(2)
                                        .add("this is a list, in case you didn't notice")
                                        .add(12))));
        MOD.createConfig("sollib/test", 1.0, builder);

        SolRegistries.RELOADER.register(manager -> SolLib.LOG.info("AYA", hello.getRaw(), hello.get(), exists.getRaw(), exists.get()));
    }


    public static void sasha() {
        EntityHolder e = MOD.getRegistrar(EntityHolder.class).register("khto_yoho_zna_sho", () -> EntityType.Builder.of(Pig::new, MobCategory.CREATURE)
                .sized(1f, 1f)
                .build("khto_yoho_zna_sho")
        );
    }
}
