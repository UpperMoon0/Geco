package com.nstut.fabric.data;

import com.nstut.Geko;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class GekoFabricDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        
        // Add the worldgen data provider that outputs to common module
        pack.addProvider(FabricWorldGenDataProvider::new);
    }
    
    @Override
    public String getEffectiveModId() {
        return Geko.MOD_ID;
    }
}
