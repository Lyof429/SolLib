package net.lcc.sollib.api.client.model;

import net.lcc.sollib.api.client.model.i.IModelPartExtension;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ModelPartUtils {
    /**
     * @param texturedData LayerDefinition to extract model parts from
     * @return {@code List<ModelPart>}
     * @since 1.0.0
     */
    public static List<ModelPart> collectAllModelParts(LayerDefinition texturedData) {
        ModelPart root = texturedData.bakeRoot();
        List<ModelPart> parts = new ArrayList<>();
        collectRecursive(root, parts);
        return parts;
    }

    /**
     * @param partSize Estimate size of required model part to look up for
     * @param parts    List of model parts to look into
     * @return {@link ModelPart}
     * @throws NullPointerException in cases of absence of looking part, method may return null
     * @since 1.0.0
     */
    @Nullable
    public static ModelPart getPart(Triple<Float, Float, Float> partSize, List<ModelPart> parts) {
        for (ModelPart part : parts) {
            if (((IModelPartExtension) (Object) part).findPartBySize(partSize.getLeft(), partSize.getMiddle(), partSize.getRight()).isPresent())
                return ((IModelPartExtension) (Object) part).findPartBySize(partSize.getLeft(), partSize.getMiddle(), partSize.getRight()).get();
        }
        return parts.get(0);
    }

    /**
     * @param partName Estimate name of required model part to look up for
     * @param parts List of model parts to look into
     * @return {@link ModelPart}
     * @throws NullPointerException in cases of absence of looking part, method may return null
     * @since 1.0.0
     */
    @Nullable
    public static ModelPart getPart(String partName, List<ModelPart> parts) {
        for (ModelPart part : parts) {
            if (((IModelPartExtension) (Object) part).findPartByName(partName).isPresent())
                return ((IModelPartExtension) (Object) part).findPartByName(partName).get();
        }
        return parts.get(0);
    }

    /**
     * @param current ModelPart to extract child parts from
     * @param out Result of model parts extraction
     * @since 1.0.0
     */
    private static void collectRecursive(ModelPart current, List<ModelPart> out) {
        out.add(current);
        for (ModelPart child : ((IModelPartExtension) (Object) current).getChildren().values())
            collectRecursive(child, out);
    }
}