package net.lcc.sollib;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.lcc.sollib.datagen.*;

public class SolLibDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(SBlockTagProvider::new);
        pack.addProvider(SItemTagProvider::new);
        pack.addProvider(SBlockLootTableProvider::new);
        pack.addProvider(SModelProvider::new);
        pack.addProvider(SRecipeProvider::new);
    }
}
