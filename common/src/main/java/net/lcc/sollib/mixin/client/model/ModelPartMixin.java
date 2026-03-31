package net.lcc.sollib.mixin.client.model;

import net.lcc.sollib.api.client.model.i.IModelPartExtension;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mixin(ModelPart.class)
public class ModelPartMixin implements IModelPartExtension {
    @Shadow
    @Final
    private Map<String, ModelPart> children;

    @Shadow
    @Final
    private List<ModelPart.Cube> cubes;

    @Override
    public Optional<ModelPart> findPartByName(String name) {
        if (this.children.containsKey(name)) {
            return Optional.of(this.children.get(name));
        }

        for (ModelPart child : this.children.values()) {
            Optional<ModelPart> found = ((IModelPartExtension) (Object) child).findPartByName(name);
            if (found.isPresent()) {
                return found;
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<ModelPart> findPartBySize(float x, float y, float z) {
        for (ModelPart.Cube cuboid : this.cubes) {
            float sizeX = cuboid.maxX - cuboid.minX;
            float sizeY = cuboid.maxY - cuboid.minY;
            float sizeZ = cuboid.maxZ - cuboid.minZ;

            if (Math.abs(sizeX - x) < 0.01f &&
                    Math.abs(sizeY - y) < 0.01f &&
                    Math.abs(sizeZ - z) < 0.01f) {
                return Optional.of((ModelPart) (Object) this);
            }
        }

        for (ModelPart child : this.children.values()) {
            Optional<ModelPart> found = ((IModelPartExtension) (Object) child).findPartBySize(x, y, z);
            if (found.isPresent()) {
                return found;
            }
        }

        return Optional.empty();
    }

    @Override
    public Map<String, ModelPart> getChildren() {
        return this.children;
    }
}