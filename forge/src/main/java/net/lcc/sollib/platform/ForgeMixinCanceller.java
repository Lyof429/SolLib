package net.lcc.sollib.platform;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import net.lcc.sollib.SolLib;
import net.minecraftforge.fml.ModList;

import java.util.List;

public class ForgeMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targets, String mixin) {
        if (mixin.equals("net.lcc.sollib.mixin.common.config.GsonHelperMixin")
                && ModList.get().isLoaded("alltheleaks")) {
            SolLib.MOD.getLogger().info("Disabled GsonHelperMixin as AllTheLeaks is loaded");
            return true;
        }
        return false;
    }
}
