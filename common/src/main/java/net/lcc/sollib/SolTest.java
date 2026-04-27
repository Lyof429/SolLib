package net.lcc.sollib;

import net.lcc.sollib.api.common.config.ConfigEntry;
import net.lcc.sollib.api.common.config.IConfigurable;
import net.lcc.sollib.api.common.config.SolConfig;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SolTest {
    public static final SolModContainer MOD = new SolModContainer("SolLib", "sollib");

    public static void lyof() {
        IConfigurable builder = it -> it.add("hello", "world");
        for (int i = 0; i < 10; i++)
            MOD.createConfig("sollib/test_" + i, 1.0, builder);


        ItemHolder x = MOD.getRegistrar(ItemHolder.class).register("test", () -> new Item(new Item.Properties()))
                .withFuel(5)
                .withModel(ModelTemplates.FLAT_ITEM);
        BlockHolder y = MOD.getRegistrar(BlockHolder.class).register("thing", () -> new Block(BlockBehaviour.Properties.of()))
                .withItem(it -> it.withFuel(100))
                .withSlab()
                .withStairs()
                .withFence()
                .cutout();
    }


    public static void sasha() {}
}
