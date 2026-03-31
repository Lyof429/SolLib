package net.lcc.sollib.api.client.model.i;

import net.minecraft.client.model.geom.ModelPart;

import java.util.Map;
import java.util.Optional;

public interface IModelPartExtension {
    Optional<ModelPart> findPartByName(String name);

    Optional<ModelPart> findPartBySize(float x, float y, float z);
    Map<String, ModelPart> getChildren();
}