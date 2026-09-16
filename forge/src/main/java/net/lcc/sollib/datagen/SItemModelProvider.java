package net.lcc.sollib.datagen;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.holder.ItemHolder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class SItemModelProvider extends ItemModelProvider {
    public SItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SolLib.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        SolRegistries.MOD.iterate(ItemHolder.class, holder -> {
            if (holder.hasModel())
                generateItemModel(holder.get(), holder.getModel());
        });
    }

    protected void generateItemModel(Item item, ModelTemplate modelTemplate) {
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(item);
        if (registryName == null) return;

        String path = registryName.getPath();

        ResourceLocation parentLoc = new ResourceLocation("minecraft", "item/generated");
        Set<TextureSlot> requiredSlots = Collections.singleton(TextureSlot.LAYER0);
        String suffix = "";

        try {
            Field modelField = ModelTemplate.class.getDeclaredField("model");
            modelField.setAccessible(true);
            Optional<ResourceLocation> modelOpt = (Optional<ResourceLocation>) modelField.get(modelTemplate);
            if (modelOpt.isPresent()) parentLoc = modelOpt.get();

            Field slotsField = ModelTemplate.class.getDeclaredField("requiredSlots");
            slotsField.setAccessible(true);
            requiredSlots = (Set<TextureSlot>) slotsField.get(modelTemplate);

            Field suffixField = ModelTemplate.class.getDeclaredField("suffix");
            suffixField.setAccessible(true);
            Optional<String> suffixOpt = (Optional<String>) suffixField.get(modelTemplate);
            if (suffixOpt.isPresent()) suffix = suffixOpt.get();

        } catch (Exception e) {
            System.err.println("Failed to reflect into ModelTemplate for item: " + path);
            e.printStackTrace();
        }

        String modelName = path + suffix;
        ItemModelBuilder builder = withExistingParent(modelName, parentLoc);

        ResourceLocation textureLoc = modLoc("item/" + path);
        for (TextureSlot slot : requiredSlots) {
            builder.texture(slot.getId(), textureLoc);
        }
    }
}
