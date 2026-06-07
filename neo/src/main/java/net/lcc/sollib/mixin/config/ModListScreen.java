package net.lcc.sollib.mixin.config;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lcc.sollib.api.client.ui.config.SolConfigScreen;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforgespi.language.IModInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(value = net.neoforged.neoforge.client.gui.ModListScreen.class, remap = false)
public class ModListScreen {
    @WrapOperation(
            method = "updateCache",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/client/gui/IConfigScreenFactory;getForMod(Lnet/neoforged/neoforgespi/language/IModInfo;)Ljava/util/Optional;"
            )
    )
    private static Optional<IConfigScreenFactory> updateSolConfigScreen(IModInfo selectedMod, Operation<Optional<IConfigScreenFactory>> original) {
        SolModContainer modContainer = SolRegistries.MOD.get(selectedMod.getModId());
        Optional<IConfigScreenFactory> result = original.call(selectedMod);
        if (modContainer != null && result.isEmpty())
            return Optional.of((minecraft, screen) -> new SolConfigScreen(modContainer, screen));
        return result;
    }

    @WrapOperation(
            method = "displayModConfig",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/client/gui/IConfigScreenFactory;getForMod(Lnet/neoforged/neoforgespi/language/IModInfo;)Ljava/util/Optional;"
            )
    )
    private static Optional<IConfigScreenFactory> displaySolConfigScreen(IModInfo selectedMod, Operation<Optional<IConfigScreenFactory>> original) {
        SolModContainer modContainer = SolRegistries.MOD.get(selectedMod.getModId());
        Optional<IConfigScreenFactory> result = original.call(selectedMod);
        if (modContainer != null && result.isEmpty())
            return Optional.of((minecraft, screen) -> new SolConfigScreen(modContainer, screen));
        return result;
    }
}
