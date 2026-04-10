package net.lcc.sollib.mixin.client.model;

import net.lcc.sollib.SolLib;
import net.lcc.sollib.api.client.model.inject.IModelPartsAccessor;
import net.lcc.sollib.api.client.model.ModelPartUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@Mixin(EntityModel.class)
public class EntityModelMixin implements IModelPartsAccessor {
    @Unique
    private List<ModelPart> modelParts;
    @Unique
    private boolean triedCollect = false;
    @Unique
    private boolean usedStatic;
    @Unique
    private String staticMethodName;

    @Override
    public List<ModelPart> getAllModelParts() {
        if (!triedCollect) {
            triedCollect = true;
            SolLib.LOG.debug("[ModelParts] Start collecting for %s".formatted(this.getClass().getSimpleName()));
            collectParts();
            SolLib.LOG.debug("[ModelParts] Done. Path=%s, count=%d".formatted(
                    (modelParts != null && !modelParts.isEmpty()) ? (usedStatic ? ("static:" + staticMethodName + "()") : "fields") : "NONE",
                    modelParts == null ? 0 : modelParts.size()));
        }
        return modelParts != null ? modelParts : List.of();
    }

    @Unique
    private void collectParts() {
        Class<?> cls = this.getClass();
        for (Method m : cls.getDeclaredMethods()) {
            if (Modifier.isStatic(m.getModifiers())
                    && m.getParameterCount() == 0
                    && LayerDefinition.class.isAssignableFrom(m.getReturnType())) {
                try {
                    m.setAccessible(true);
                    LayerDefinition data = (LayerDefinition) m.invoke(null);
                    modelParts = ModelPartUtils.collectAllModelParts(data);
                    usedStatic = true;
                    staticMethodName = m.getName();
                    return;
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                }
            }
        }
        usedStatic = false;
        staticMethodName = null;
        modelParts = new ArrayList<>();
        Class<?> walk = cls;
        while (walk != null && EntityModel.class.isAssignableFrom(walk)) {
            for (Field f : walk.getDeclaredFields()) {
                if (ModelPart.class.isAssignableFrom(f.getType())) {
                    try {
                        f.setAccessible(true);
                        ModelPart p = (ModelPart) f.get(this);
                        if (p != null && !modelParts.contains(p)) {
                            modelParts.add(p);
                        }
                    } catch (IllegalAccessException ignored) {
                    }
                }
            }
            walk = walk.getSuperclass();
        }
    }
}