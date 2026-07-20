package net.lcc.sollib;

import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.config.builder.IConfigurable;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.lcc.sollib.core.Identifier;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SolTest {
    public static final SolModContainer MOD = new SolModContainer("SolTest", "soltest");

    public static void lyof() {
        SolLib.MOD.createConfig("soltest", 1, builder -> builder
                .addObject("main", main -> main
                        .comment("This needs testing")
                        .add("text", "hello world")
                        .comment("Here be the universe")
                        .add("answer", 42)
                )
        );
    }

    public static void sasha() {
    }
}
