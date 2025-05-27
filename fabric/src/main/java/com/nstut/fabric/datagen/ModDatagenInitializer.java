package com.nstut.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;

import java.util.concurrent.CompletableFuture;

public class ModDatagenInitializer implements DataGeneratorEntrypoint {
    
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        
        pack.addProvider((FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) -> 
            new FabricDynamicRegistryProvider(output, registriesFuture) {                @Override
                protected void configure(HolderLookup.Provider registries, Entries entries) {
                    // Register configured features
                    entries.addAll(registries.lookupOrThrow(Registries.CONFIGURED_FEATURE));
                    // Register placed features  
                    entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
                }

                @Override
                public String getName() {
                    return "Geko World Generation";
                }
            }
        );
    }
}
