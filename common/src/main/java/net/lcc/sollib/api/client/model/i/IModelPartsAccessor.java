package net.lcc.sollib.api.client.model.i;

import net.minecraft.client.model.geom.ModelPart;

import java.util.List;

public interface IModelPartsAccessor {
    /**
     * Returns list containing each model part gathered from entity model
     * @return {@code List<ModelPart>}
     */
    List<ModelPart> getAllModelParts();
}