package net.lcc.sollib;

import net.lcc.sollib.api.common.registry.SolModContainer;
import net.lcc.sollib.api.common.registry.holder.BlockHolder;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.PushReaction;

public class SolTest {
    private static final SolModContainer MOD = SolLib.MOD;

    public static void lyof() {
        BlockSetType EDEN_SETTYPE = BlockSetType.OAK;
        WoodType EDEN_WOODTYPE = WoodType.OAK;

        BlockBehaviour.Properties edenMaterial =
                BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS);
        BlockBehaviour.Properties edenPassableMaterial =
                BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)
                        .noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY);

        BlockHolder EDEN_SIGN = MOD.register(BlockHolder.class, "eden_sign", () -> new StandingSignBlock(edenPassableMaterial, EDEN_WOODTYPE))
                .withTags(BlockTags.SIGNS, BlockTags.STANDING_SIGNS, BlockTags.WALL_POST_OVERRIDE, BlockTags.MINEABLE_WITH_AXE);
        BlockHolder EDEN_WALL_SIGN = MOD.register(BlockHolder.class, "eden_wall_sign", () -> new WallSignBlock(edenPassableMaterial, EDEN_WOODTYPE))
                .withTags(BlockTags.SIGNS, BlockTags.WALL_SIGNS, BlockTags.WALL_POST_OVERRIDE, BlockTags.MINEABLE_WITH_AXE);
        BlockHolder EDEN_HANGING_SIGN = MOD.register(BlockHolder.class, "eden_hanging_sign", () -> new CeilingHangingSignBlock(edenPassableMaterial, EDEN_WOODTYPE))
                .withTags(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS, BlockTags.MINEABLE_WITH_AXE);
        BlockHolder EDEN_WALL_HANGING_SIGN = MOD.register(BlockHolder.class, "eden_wall_hanging_sign", () -> new WallHangingSignBlock(edenPassableMaterial, EDEN_WOODTYPE))
                .withTags(BlockTags.ALL_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS, BlockTags.MINEABLE_WITH_AXE);
        BlockHolder EDEN_PLANKS = MOD.register(BlockHolder.class, "eden_planks", () -> new Block(edenMaterial))
                .withSetPrefix("eden_plank")
                .dropsSelf().withTags(BlockTags.PLANKS, BlockTags.MINEABLE_WITH_AXE)
                .withItem(it -> it.withTags(ItemTags.PLANKS))
                .withSlab(it -> it.dropsSelf().withTags(BlockTags.WOODEN_SLABS, BlockTags.MINEABLE_WITH_AXE))
                .withStairs(it -> it.dropsSelf().withTags(BlockTags.WOODEN_STAIRS, BlockTags.MINEABLE_WITH_AXE))
                .withFence(it -> it.dropsSelf().withTags(BlockTags.WOODEN_FENCES, BlockTags.MINEABLE_WITH_AXE))
                .withFenceGate(EDEN_WOODTYPE).withFenceGate(it -> it.dropsSelf().withTags(BlockTags.WOODEN_FENCES, BlockTags.MINEABLE_WITH_AXE))
                .withButton(EDEN_SETTYPE, 20, true).withButton(it -> it.dropsSelf().withTags(BlockTags.WOODEN_BUTTONS, BlockTags.MINEABLE_WITH_AXE))
                .withPressurePlate(PressurePlateBlock.Sensitivity.EVERYTHING, EDEN_SETTYPE).withPressurePlate(it -> it.dropsSelf().withTags(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.MINEABLE_WITH_AXE))
                .withSign(EDEN_SIGN, EDEN_WALL_SIGN);

        ItemHolder IEDEN_SIGN = MOD.register(ItemHolder.class, "eden_sign",
                () -> new SignItem(new Item.Properties(), EDEN_SIGN.get(), EDEN_WALL_SIGN.get()))
                .withTags(ItemTags.SIGNS)
                .withFuel(200);
        ItemHolder IEDEN_HANGING_SIGN = MOD.register(ItemHolder.class, "eden_hanging_sign",
                () -> new HangingSignItem(EDEN_HANGING_SIGN.get(), EDEN_WALL_HANGING_SIGN.get(), new Item.Properties()))
                .withTags(ItemTags.HANGING_SIGNS)
                .withFuel(200);

        EDEN_SIGN.dropsOther(IEDEN_SIGN::get);
        EDEN_WALL_SIGN.dropsOther(IEDEN_SIGN::get);
        EDEN_HANGING_SIGN.dropsOther(IEDEN_HANGING_SIGN::get);
        EDEN_WALL_HANGING_SIGN.dropsOther(IEDEN_HANGING_SIGN::get);
    }


    public static void sasha() {
    }
}
