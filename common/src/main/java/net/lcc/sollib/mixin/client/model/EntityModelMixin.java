package net.lcc.sollib.mixin.client.model;

import net.lcc.sollib.api.client.model.ModelPartUtils;
import net.lcc.sollib.api.client.model.inject.IModelPartsAccessor;
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
    @Unique private List<ModelPart> sol_modelParts;
    @Unique private boolean sol_triedCollect = false;
    @Unique private boolean sol_usedStatic;
    @Unique private String sol_staticMethodName;

    @Override
    public List<ModelPart> getAllModelParts() {
        if (!sol_triedCollect) {
            sol_triedCollect = true;
            ModelPartUtils.LOG.debug("[ModelParts] Start collecting for %s".formatted(this.getClass().getSimpleName()));
            sol_collectParts();
            ModelPartUtils.LOG.debug("[ModelParts] Done. Path=%s, count=%d".formatted(
                    (sol_modelParts != null && !sol_modelParts.isEmpty()) ? (sol_usedStatic ? ("static:" + sol_staticMethodName + "()") : "fields") : "NONE",
                    sol_modelParts == null ? 0 : sol_modelParts.size()));
        }
        return sol_modelParts != null ? sol_modelParts : List.of();
    }

    @Unique
    private void sol_collectParts() {
        Class<?> cls = this.getClass();
        for (Method m : cls.getDeclaredMethods()) {
            if (Modifier.isStatic(m.getModifiers())
                    && m.getParameterCount() == 0
                    && LayerDefinition.class.isAssignableFrom(m.getReturnType())) {
                try {
                    m.setAccessible(true);
                    LayerDefinition data = (LayerDefinition) m.invoke(null);
                    sol_modelParts = ModelPartUtils.collectAllModelParts(data);
                    sol_usedStatic = true;
                    sol_staticMethodName = m.getName();
                    return;
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                }
            }
        }
        sol_usedStatic = false;
        sol_staticMethodName = null;
        sol_modelParts = new ArrayList<>();
        Class<?> walk = cls;
        while (walk != null && EntityModel.class.isAssignableFrom(walk)) {
            for (Field f : walk.getDeclaredFields()) {
                if (ModelPart.class.isAssignableFrom(f.getType())) {
                    try {
                        f.setAccessible(true);
                        ModelPart p = (ModelPart) f.get(this);
                        if (p != null && !sol_modelParts.contains(p)) {
                            sol_modelParts.add(p);
                        }
                    } catch (IllegalAccessException ignored) {
                    }
                }
            }
            walk = walk.getSuperclass();
        }
    }
}