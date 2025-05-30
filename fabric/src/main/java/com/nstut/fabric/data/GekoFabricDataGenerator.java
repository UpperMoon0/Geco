package com.nstut.fabric.data;

import com.nstut.Geko;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import com.nstut.registry.ModWorldGeneration; // Assuming ModWorldGeneration is in this package

public class GekoFabricDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        
        // Add the worldgen data provider that outputs to common module
        pack.addProvider(CommonWorldGenDataProvider::new);
        
        // Add the NeoForge biome modifier provider (cross-module generation)
        pack.addProvider(NeoForgeBiomeModifierDataProvider::new);
    }
    
    @Override
    public String getEffectiveModId() {
        return Geko.MOD_ID;
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.CONFIGURED_FEATURE, ModWorldGeneration::bootstrapConfiguredFeatures);
        registryBuilder.add(Registries.PLACED_FEATURE, ModWorldGeneration::bootstrapPlacedFeatures);
    }
}
